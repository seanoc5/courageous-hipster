package com.oconeco.courageous.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.service.ParserService;
import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import com.oconeco.courageous.service.dto.BraveSearchResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ParserServiceImpl implements ParserService {

    private static final Logger LOG = LoggerFactory.getLogger(ParserServiceImpl.class);

    private static final String NO_DESCRIPTION = "No description available";
    private static final String NO_FAVICON = "No favicon available";
    private static final String WEB_PATH = "web";
    private static final String RESULTS_PATH = "results";
    private static final String META_URL_PATH = "meta_url";
    private static final String FAVICON_KEY = "favicon";
    private static final String TITLE_KEY = "title";
    private static final String URL_KEY = "url";
    private static final String DESCRIPTION_KEY = "description";

    @Override
    public BraveSearchResponseDTO parseDTO(String jsonContent) throws IOException {
        if (StringUtils.isBlank(jsonContent)) {
            throw new IllegalArgumentException("JSON content cannot be null or empty.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        List<BraveSearchResultDTO> results = extractResults(rootNode);

        BraveSearchResponseDTO responseDTO = new BraveSearchResponseDTO();
        responseDTO.setResults(results);

        return responseDTO;
    }

    /**
     * Extracts search results from the root JSON node.
     */
    private List<BraveSearchResultDTO> extractResults(JsonNode rootNode) {
        List<BraveSearchResultDTO> results = new ArrayList<>();
        JsonNode webResults = rootNode.path(WEB_PATH).path(RESULTS_PATH);

        if (webResults.isMissingNode() || !webResults.isArray()) {
            LOG.warn("No valid results found in the JSON structure.");
            return results;
        }

        for (JsonNode resultNode : webResults) {
            BraveSearchResultDTO resultDTO = parseResultNode(resultNode);
            if (resultDTO != null) {
                results.add(resultDTO);
            }
        }
        return results;
    }

    /**
     * Parses an individual result node into a DTO.
     */
    private BraveSearchResultDTO parseResultNode(JsonNode resultNode) {
        String title = resultNode.path(TITLE_KEY).asText();
        String url = resultNode.path(URL_KEY).asText();
        String description = resultNode.path(DESCRIPTION_KEY).asText();

        if (title == null || url == null || title.isEmpty() || url.isEmpty()) {
            LOG.warn("Skipping result node due to missing title or URL.");
            return null;
        }

        BraveSearchResultDTO resultDTO = new BraveSearchResultDTO();
        resultDTO.setTitle(title);
        resultDTO.setUrl(url);
        resultDTO.setDescription(description.isEmpty() ? NO_DESCRIPTION : description);

        String faviconSrc = extractFavicon(resultNode);
        resultDTO.setFaviconSrc(faviconSrc);

        return resultDTO;
    }

    /**
     * Extracts the favicon source URL from a result node.
     */
    private String extractFavicon(JsonNode resultNode) {
        JsonNode metaUrlNode = resultNode.path(META_URL_PATH);
        String faviconSrc = metaUrlNode.path(FAVICON_KEY).asText();

        if (faviconSrc == null || faviconSrc.isEmpty()) {
            return NO_FAVICON;
        }
        return faviconSrc;
    }
}
