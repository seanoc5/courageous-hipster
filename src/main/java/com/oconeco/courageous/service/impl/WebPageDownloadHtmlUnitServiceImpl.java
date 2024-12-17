package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.service.WebPageDownloadHtmlUnitService;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebPageDownloadHtmlUnitServiceImpl implements WebPageDownloadHtmlUnitService {

    private static final Logger LOG = LoggerFactory.getLogger(WebPageDownloadHtmlUnitServiceImpl.class);
    private final WebClient webClient;

    @Autowired
    public WebPageDownloadHtmlUnitServiceImpl(org.htmlunit.WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Download content using HtmlUnit with JavaScript enabled.
     */
    @Override
    public String downloadWithHtmlUnit(String uri) throws IOException {
        try {
            HtmlPage page = webClient.getPage(uri);
            return page.asXml();
        } catch (Exception e) {
            LOG.error("HtmlUnit download failed for URI: {}", uri, e);
            throw new IOException("Error fetching content with HtmlUnit for URI: " + uri, e);
        }
    }
}
