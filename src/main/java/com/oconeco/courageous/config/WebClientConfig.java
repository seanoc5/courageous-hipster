package com.oconeco.courageous.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.oconeco.courageous.service.constants.FetcherConstants.*;


@Configuration
public class WebClientConfig {

    /**
     * Bean for Spring WebClient.
     * Configures a reactive HTTP client with proper timeouts and headers.
     */
    @Bean
    public WebClient springWebClient() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_MILLISECONDS)
            .responseTimeout(Duration.ofMillis(READ_WRITE_TIMEOUT_MILLISECONDS))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(READ_WRITE_TIMEOUT_MILLISECONDS / 1000))
                    .addHandlerLast(new WriteTimeoutHandler(READ_WRITE_TIMEOUT_MILLISECONDS / 1000))
            )
            .followRedirect(true);

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(BUFFER_MEMORY_BYTES))
            .build();
    }

    /**
     * Bean for HtmlUnit WebClient.
     * Configures a headless browser for web scraping or automation tasks.
     */
    @Bean
    public org.htmlunit.WebClient htmlUnitWebClient() {
        org.htmlunit.WebClient webClient = new org.htmlunit.WebClient();

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false); // Ignore JS errors
        webClient.getOptions().setTimeout(TIMEOUT_MILLISECONDS);

        webClient.addRequestHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        webClient.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
        webClient.addRequestHeader("Connection", "keep-alive");

        return webClient;
    }

    /**
     * Bean for RestTemplate.
     * Provides a simple synchronous HTTP client.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Bean for task executor.
     * Configures a thread pool with a fixed size.
     */
    @Bean
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }
}
