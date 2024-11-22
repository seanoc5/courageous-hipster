package com.oconeco.courageous.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/*
 * DTO to Brave Search Response
 *
 * */

public class BraveSearchResponseDTO {

    @JsonProperty("results")
    private List<BraveSearchResultDTO> results;

    @JsonProperty("type")
    private String type;

    private String responseBody;
    private int statusCode;

    // Getters and Setters
    public List<BraveSearchResultDTO> getResults() {
        return results;
    }

    public void setResults(List<BraveSearchResultDTO> results) {
        this.results = results;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
