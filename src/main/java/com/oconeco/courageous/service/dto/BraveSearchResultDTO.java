package com.oconeco.courageous.service.dto;

public class BraveSearchResultDTO {

    private String url;
    private String title;
    private String description;
    private String faviconSrc;

    // Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFaviconSrc() {
        return faviconSrc;
    }

    public void setFaviconSrc(String faviconSrc) {
        this.faviconSrc = faviconSrc;
    }
}
