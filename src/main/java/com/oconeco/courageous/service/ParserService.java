package com.oconeco.courageous.service;

import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;

import java.io.IOException;

public interface ParserService {
    BraveSearchResponseDTO parseDTO(String jsonContent) throws IOException;
}
