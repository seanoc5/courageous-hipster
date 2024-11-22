package com.oconeco.courageous.service;

import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;
import java.io.IOException;

public interface HtmlParserService {
    BraveSearchResponseDTO parseHtmlToDTO(String htmlContent) throws IOException;
}
