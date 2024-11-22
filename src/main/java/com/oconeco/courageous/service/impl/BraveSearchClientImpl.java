package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.service.BraveSearchClient;
import com.oconeco.courageous.service.HtmlParserService;
import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import com.oconeco.courageous.web.rest.errors.SearchException;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BraveSearchClientImpl implements BraveSearchClient {

    private static final Logger LOG = LoggerFactory.getLogger(BraveSearchClientImpl.class);
    private final RestTemplate restTemplate;

    @Value("${brave.search.api.url}")
    private String apiUrl;

    @Autowired
    private HtmlParserService htmlParserService;

    public BraveSearchClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public BraveSearchResponseDTO search(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer ");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/search?q=" + query, HttpMethod.GET, entity, String.class);

        try {
            return htmlParserService.parseHtmlToDTO(response.getBody());
        } catch (IOException e) {
            throw new SearchException("Failed to parse search results", e);
        }
    }
}
