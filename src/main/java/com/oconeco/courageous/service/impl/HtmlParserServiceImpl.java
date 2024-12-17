package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.service.HtmlParserService;
import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import com.oconeco.courageous.service.dto.BraveSearchResultDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.oconeco.courageous.service.impl.BraveSearchClientImpl.MAX_SEARCH_RESULTS;

@Service
public class HtmlParserServiceImpl implements HtmlParserService {


    @Override
    public BraveSearchResponseDTO parseHtmlToDTO(String htmlContent) throws IOException {
        Document document = Jsoup.parse(htmlContent);

        List<BraveSearchResultDTO> results = new ArrayList<>();

        for (Element resultElement : document.select("div.snippet")) {

            if (results.size() > MAX_SEARCH_RESULTS) break;
            String title = resultElement.select("div.title").text();
            String url = resultElement.select("a").attr("href");

            if (title == null || url == null || title.isEmpty() || url.isEmpty()) {
                continue;
            }
            BraveSearchResultDTO resultDTO = new BraveSearchResultDTO();
            resultDTO.setTitle(title);
            resultDTO.setUrl(url);

            String description = resultElement.select("div.snippet-description").text();
            if (description.isEmpty()) {
                description = resultElement.select("p.description").text();
                if (description.isEmpty()) {
                    description = "No description available";
                }
            }
            resultDTO.setDescription(description);

            // Extract favicon src
            String faviconSrc = resultElement.select("div.favicon-wrapper img.favicon").attr("src");
            if (faviconSrc != null && !faviconSrc.isEmpty()) {
                resultDTO.setFaviconSrc(faviconSrc);
            } else {
                resultDTO.setFaviconSrc("No favicon available");
            }

            results.add(resultDTO);
        }

        BraveSearchResponseDTO responseDTO = new BraveSearchResponseDTO();
        responseDTO.setResults(results);
        return responseDTO;
    }
}
