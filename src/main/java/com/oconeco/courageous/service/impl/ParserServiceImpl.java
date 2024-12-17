package com.oconeco.courageous.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oconeco.courageous.service.ParserService;
import com.oconeco.courageous.service.constants.ParserConstants;
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

import static com.oconeco.courageous.service.impl.BraveSearchClientImpl.MAX_SEARCH_RESULTS;


@Service
@Slf4j
public class ParserServiceImpl implements ParserService {

    private static final Logger LOG = LoggerFactory.getLogger(ParserServiceImpl.class);

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
        JsonNode webResults = rootNode.path(ParserConstants.WEB_PATH).path(ParserConstants.RESULTS_PATH);

        if (webResults.isMissingNode() || !webResults.isArray()) {
            LOG.warn("No valid results found in the JSON structure.");
            return results;
        }

        for (JsonNode resultNode : webResults) {
            if (results.size() >= MAX_SEARCH_RESULTS) break;

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
        String title = resultNode.path(ParserConstants.TITLE_KEY).asText();
        String url = resultNode.path(ParserConstants.URL_KEY).asText();
        String description = resultNode.path(ParserConstants.DESCRIPTION_KEY).asText();

        if (title == null || url == null || title.isEmpty() || url.isEmpty()) {
            LOG.warn("Skipping result node due to missing title or URL.");
            return null;
        }

        BraveSearchResultDTO resultDTO = new BraveSearchResultDTO();
        resultDTO.setTitle(title);
        resultDTO.setUrl(url);
        resultDTO.setDescription(description.isEmpty() ? ParserConstants.NO_DESCRIPTION : description);

        String faviconSrc = extractFavicon(resultNode);
        resultDTO.setFaviconSrc(faviconSrc);

        return resultDTO;
    }

    /**
     * Extracts the favicon source URL from a result node.
     */
    private String extractFavicon(JsonNode resultNode) {
        JsonNode metaUrlNode = resultNode.path(ParserConstants.META_URL_PATH);
        String faviconSrc = metaUrlNode.path(ParserConstants.FAVICON_KEY).asText();

        if (faviconSrc == null || faviconSrc.isEmpty()) {
            return ParserConstants.NO_FAVICON;
        }
        return faviconSrc;
    }
}
