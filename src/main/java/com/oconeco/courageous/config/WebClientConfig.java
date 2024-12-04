package com.oconeco.courageous.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .clientConnector(
                new ReactorClientHttpConnector(
                    HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                        .responseTimeout(Duration.ofSeconds(10))
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10)).addHandlerLast(new WriteTimeoutHandler(10)))
                        .followRedirect(true)
                )
            )
            .defaultHeader(HttpHeaders.ACCEPT, "*/*")
            .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (compatible; YourBot/1.0; +https://yourwebsite.com/bot)") // Set User-Agent here
            .codecs(clientCodecConfigurer -> {
                clientCodecConfigurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB
            })
            .filter(
                ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                    return Mono.just(clientRequest);
                })
            )
            .filter(
                ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                    log.debug("Response: {}", clientResponse.statusCode());
                    return Mono.just(clientResponse);
                })
            )
            .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
