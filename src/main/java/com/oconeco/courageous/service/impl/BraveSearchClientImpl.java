package com.oconeco.courageous.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.domain.SearchConfiguration;
import com.oconeco.courageous.service.BraveSearchClient;
import com.oconeco.courageous.service.ParserService;
import com.oconeco.courageous.service.constants.HttpHeadersConstants;
import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import com.oconeco.courageous.web.rest.errors.SearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class BraveSearchClientImpl implements BraveSearchClient {

    public static int MAX_SEARCH_RESULTS;

    private static final Logger LOG = LoggerFactory.getLogger(BraveSearchClientImpl.class);
    private final RestTemplate restTemplate;
    private final ParserService htmlParserService;

    public BraveSearchClientImpl(RestTemplate restTemplate, ParserService htmlParserService) {
        this.restTemplate = restTemplate;
        this.htmlParserService = htmlParserService;
    }

    @Override
    public BraveSearchResponseDTO search(String query, SearchConfiguration config) {
        if (config == null || config.getHeadersJson() == null || config.getHeadersJson().isEmpty()) {
            throw new IllegalArgumentException("Invalid search configuration or headers JSON.");
        }

        // Extract the X-Subscription-Token from headersJson
        String subscriptionToken;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(config.getHeadersJson());
            subscriptionToken = jsonNode.get(HttpHeadersConstants.SUBSCRIPTION_TOKEN).asText();
            LOG.debug("{} : {}", HttpHeadersConstants.SUBSCRIPTION_TOKEN, subscriptionToken);
            JsonNode paramNode = objectMapper.readTree(config.getParamsJson());
            if (paramNode.has(HttpHeadersConstants.RESULT_COUNT)) {
                MAX_SEARCH_RESULTS = paramNode.get(HttpHeadersConstants.RESULT_COUNT).asInt();
            } else {
                throw new IllegalArgumentException("Missing 'count' field in paramsJson");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid headersJson format", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersConstants.AUTHORIZATION, HttpHeadersConstants.BEARER_PREFIX + subscriptionToken);
        headers.set(HttpHeadersConstants.SUBSCRIPTION_TOKEN, subscriptionToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeadersConstants.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        ResponseEntity<String> response = restTemplate.exchange(
            config.getUrl() + "?q=" + encodedQuery,
            HttpMethod.GET,
            entity,
            String.class
        );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new SearchException("Empty or null response body");
        }
        try {
            return htmlParserService.parseDTO(response.getBody());
        } catch (IOException e) {
            throw new SearchException("Failed to parse search results", e);
        }
    }
}
