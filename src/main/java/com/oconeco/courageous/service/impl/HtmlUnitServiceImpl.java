package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.service.WebPageDownloadService;
import java.io.IOException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

@Service
public class HtmlUnitServiceImpl implements WebPageDownloadService {

    @Override
    public String download(String uri) throws IOException {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(uri);
            return page.asXml();
        }
    }
}
