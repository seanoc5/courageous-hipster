package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Content;
import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.ContentRepository;
import com.oconeco.courageous.repository.SearchResultRepository;
import com.oconeco.courageous.service.FetcherService;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class FetcherServiceImpl implements FetcherService {

    private static final Logger LOG = LoggerFactory.getLogger(FetcherServiceImpl.class);
    private final ContentRepository contentRepository;
    private final WebClient webClient;

    @Value("${location.folder.path}")
    private String CONTENT_DIR;

    public FetcherServiceImpl(ContentRepository contentRepository, SearchResultRepository searchResultRepository, WebClient webClient) {
        this.contentRepository = contentRepository;
        this.webClient = webClient;
    }

    @Override
    @Async
    public CompletableFuture<Void> fetchContentForSearch(SearchResult searchResult) {
        Executor executor = Executors.newFixedThreadPool(20);

        List<CompletableFuture<Void>> futures = searchResult
            .getContents()
            .stream()
            .map(content -> CompletableFuture.runAsync(() -> downloadAndProcess(content), executor))
            .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return CompletableFuture.completedFuture(null);
    }

    private void downloadAndProcess(Content content) {
        webClient
            .get()
            .uri(content.getUri())
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response ->
                    response.statusCode() == HttpStatus.FORBIDDEN
                        ? Mono.error(new RuntimeException("Forbidden access"))
                        : Mono.error(new RuntimeException("Client error"))
            )
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(10))
            .onErrorResume(e -> {
                LOG.error("Error fetching content for URI: {}", content.getUri(), e);
                markContentAsFailed(content);
                return Mono.empty();
            })
            .flatMap(html -> processHtml(content, html))
            .doOnError(e -> LOG.error("Processing error for content ID: {}", content.getId(), e))
            .subscribe();
    }

    private Mono<Void> processHtml(Content content, String html) {
        return Mono.fromCallable(() -> {
            if (html == null || html.trim().isEmpty()) {
                markContentAsFailed(content);
                return null;
            }

            Document document = Jsoup.parse(html);

            // Fix relative URLs like link, script ,img
            document
                .select("link[href], script[src], img[src]")
                .forEach(element -> {
                    String attribute = element.tagName().equals("link") ? "href" : "src";
                    String url = element.attr(attribute);
                    element.attr(attribute, resolveUrl(content.getUri(), url));
                });

            // Save updated HTML
            String updatedHtml = document.html();
            saveHtmlToFile(content, updatedHtml);

            // Process using Readability4J
            Article article = new Readability4J(content.getUri(), updatedHtml).parse();

            // Update content
            content.setTitle(Optional.ofNullable(article.getTitle()).orElse(content.getTitle()));
            content.setDescription(Optional.ofNullable(article.getExcerpt()).orElse(content.getDescription()));
            content.setBodyText(article.getTextContent());
            content.setTextSize(Optional.ofNullable(article.getTextContent()).map(text -> (long) text.length()).orElse(0L));
            content.setLastUpdate(Instant.now());

            contentRepository.save(content);
            return null;
        })
            .onErrorResume(e -> {
                LOG.error("Error processing HTML for content ID: {}", content.getId(), e);
                markContentAsFailed(content);
                return Mono.empty();
            })
            .then();
    }

    private String resolveUrl(String baseUri, String url) {
        try {
            return new URI(baseUri).resolve(url).toString();
        } catch (URISyntaxException e) {
            LOG.error("Error resolving URL: {} relative to base: {}", url, baseUri, e);
            return url;
        }
    }

    private void saveHtmlToFile(Content content, String html) throws IOException {
        Path filePath = Paths.get(CONTENT_DIR, "content_" + content.getId() + ".html");
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, html.getBytes(StandardCharsets.UTF_8));
    }

    private void markContentAsFailed(Content content) {
        contentRepository.save(content);
    }
}
