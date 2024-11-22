package com.oconeco.courageous.service;

import com.oconeco.courageous.service.dto.BraveSearchResponseDTO;

public interface BraveSearchClient {
    BraveSearchResponseDTO search(String query);
}
