package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Content;
import com.oconeco.courageous.domain.SearchResult;
import com.oconeco.courageous.repository.ContentRepository;
import com.oconeco.courageous.service.FetcherService;
import com.oconeco.courageous.service.WebPageDownloadHtmlUnitService;
import com.oconeco.courageous.service.WebPageDownloadWebClientService;
import lombok.extern.slf4j.Slf4j;
import net.dankito.readability4j.Article;
import net.dankito.readability4j.Readability4J;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Service
@Slf4j
public class FetcherServiceImpl implements FetcherService {

    private static final Logger LOG = LoggerFactory.getLogger(FetcherServiceImpl.class);
    private static final String FILE_PREFIX = "content_";
    private static final String FILE_EXTENSION = ".html";

    private final Executor taskExecutor;
    private final ContentRepository contentRepository;
    private final WebPageDownloadHtmlUnitService htmlUnitService;
    private final WebPageDownloadWebClientService webClientService;

    @Value("${location.folder.path}")
    private String contentDirectory;

    public FetcherServiceImpl(Executor taskExecutor, ContentRepository contentRepository, WebPageDownloadHtmlUnitService htmlUnitService, WebPageDownloadWebClientService webClientService) {
        this.taskExecutor = taskExecutor;
        this.contentRepository = contentRepository;
        this.htmlUnitService = htmlUnitService;
        this.webClientService = webClientService;
    }

    @Override
    @Async
    public CompletableFuture<Void> fetchContentForSearch(SearchResult searchResult) {
        List<CompletableFuture<Void>> futures = searchResult
            .getContents()
            .stream()
            .map(content -> CompletableFuture.runAsync(() -> downloadAndProcess(content), taskExecutor))  // Using shared executor
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return CompletableFuture.completedFuture(null);
    }


    private void downloadAndProcess(Content content) {
        String html = null;

        // Try downloading using WebClient first
        try {
            html = webClientService.downloadWithWebClient(content.getUri());
            LOG.info("WebClient download success for URI: {}", content.getUri());
        } catch (IOException e) {
            LOG.error("WebClient download failed for URI: {}, retrying with HtmlUnit...", content.getUri(), e);
        }

        // If WebClient fails, retry with HtmlUnit
        if (html == null) {
            try {
                html = htmlUnitService.downloadWithHtmlUnit(content.getUri());
                LOG.info("HtmlUnit download success for URI: {}", content.getUri());
            } catch (IOException htmlUnitException) {
                LOG.error("HtmlUnit download failed for URI: {}", content.getUri(), htmlUnitException);
                markContentAsFailed(content);
                return;
            }
        }

        // Process the HTML content if it was successfully downloaded
        if (html != null) {
            processHtml(content, html).block();
        } else {
            markContentAsFailed(content);
        }
    }


    private Mono<Void> processHtml(Content content, String html) {
        return Mono.fromCallable(() -> {
                if (html == null || html.trim().isEmpty()) {
                    markContentAsFailed(content);
                    return null;
                }

                Document document = Jsoup.parse(html);
                processLinks(content.getUri(), document);

                String updatedHtml = document.html();
                saveHtmlToFile(content, updatedHtml);

                Article article = new Readability4J(content.getUri(), updatedHtml).parse();

                // Update content
                updateContent(content, article);
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


    private void processLinks(String baseUri, Document document) {
        document
            .select("link[href], script[src], img[src]")
            .forEach(element -> {
                String attribute = element.tagName().equals("link") ? "href" : "src";
                String url = element.attr(attribute);
                if (!url.startsWith("http")) {
                    element.attr(attribute, resolveUrl(baseUri, url));
                }
            });
    }

    private String resolveUrl(String baseUri, String url) {
        try {
            return new URI(baseUri).resolve(url).toString();
        } catch (URISyntaxException e) {
            LOG.error("Error resolving URL: {} relative to base: {}", url, baseUri, e);
            return url;
        }
    }

    private void updateContent(Content content, Article article) {
        content.setTitle(Optional.ofNullable(article.getTitle()).orElse(content.getTitle()));
        content.setDescription(Optional.ofNullable(article.getExcerpt()).orElse(content.getDescription()));
        content.setBodyText(article.getTextContent());
        content.setTextSize(Optional.ofNullable(article.getTextContent()).map(text -> (long) text.length()).orElse(0L));
        content.setLastUpdate(Instant.now());
    }



    private void saveHtmlToFile(Content content, String html) throws IOException {
        Path filePath = Paths.get(contentDirectory, FILE_PREFIX + content.getId() + FILE_EXTENSION);
        Files.createDirectories(filePath.getParent());
        Files.writeString(filePath, html);
    }

    private void markContentAsFailed(Content content) {
        LOG.error("Failed to process content with ID: {}, Title: {}, URI: {}",
            content.getId(), content.getTitle(), content.getUri());
    }
}
