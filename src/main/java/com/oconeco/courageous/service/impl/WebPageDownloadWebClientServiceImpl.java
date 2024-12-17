package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.service.WebPageDownloadWebClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Service
public class WebPageDownloadWebClientServiceImpl implements WebPageDownloadWebClientService {
    private static final Logger LOG = LoggerFactory.getLogger(WebPageDownloadWebClientServiceImpl.class);
    private final WebClient webClient;

    @Autowired
    public WebPageDownloadWebClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Download content using Spring WebClient with retries and user-agent.
     */
    @Override
    public String downloadWithWebClient(String uri) throws IOException {
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                LOG.info("Attempt {} to download content from URI: {}", attempt, uri);
                return webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            } catch (Exception e) {
                LOG.error("WebClient attempt {} failed for URI: {}", attempt, uri, e);
                if (attempt == maxRetries) {
                    throw new IOException("Error downloading content from URI: " + uri, e);
                }
                Mono.delay(Duration.ofSeconds(2L * attempt)).block();
            }
        }
        throw new IOException("Failed to download content after retries: " + uri);
    }

}
