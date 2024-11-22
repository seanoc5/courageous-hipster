package com.oconeco.courageous.service;

import com.oconeco.courageous.domain.SearchResult;
import java.util.concurrent.CompletableFuture;

public interface FetcherService {
    /**
     * To get fetch content for search
     */
    CompletableFuture<Void> fetchContentForSearch(SearchResult searchResult);
}
