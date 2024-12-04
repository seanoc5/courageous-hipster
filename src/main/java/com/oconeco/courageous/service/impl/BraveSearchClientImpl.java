package com.oconeco.courageous.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.service.BraveSearchClient;
import com.oconeco.courageous.service.HtmlParserService;
import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import com.oconeco.courageous.web.rest.errors.SearchException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BraveSearchClientImpl implements BraveSearchClient {

    private static final Logger LOG = LoggerFactory.getLogger(BraveSearchClientImpl.class);
    private final RestTemplate restTemplate;
    private final HtmlParserService htmlParserService;

    @Value("${brave.search.api.url}")
    private String apiUrl;

    public BraveSearchClientImpl(RestTemplate restTemplate, HtmlParserService htmlParserService) {
        this.restTemplate = restTemplate;
        this.htmlParserService = htmlParserService;
    }

    @Override
    public BraveSearchResponseDTO search(String query, String headersJson) {
        // Extract the X-Subscription-Token from headersJson
        String subscriptionToken;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(headersJson);
            subscriptionToken = jsonNode.get("X-Subscription-Token").asText();
            LOG.debug("subscriptionToken :{}", subscriptionToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid headersJson format", e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + subscriptionToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/search?q=" + query, HttpMethod.GET, entity, String.class);

        // Parse and return the response
        try {
            return htmlParserService.parseHtmlToDTO(response.getBody());
        } catch (IOException e) {
            throw new SearchException("Failed to parse search results", e);
        }
    }
}
