package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SearchResultTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SearchResult getSearchResultSample1() {
        return new SearchResult().id(1L).query("query1").type("type1").statusCode(1);
    }

    public static SearchResult getSearchResultSample2() {
        return new SearchResult().id(2L).query("query2").type("type2").statusCode(2);
    }

    public static SearchResult getSearchResultRandomSampleGenerator() {
        return new SearchResult()
            .id(longCount.incrementAndGet())
            .query(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .statusCode(intCount.incrementAndGet());
    }
}
