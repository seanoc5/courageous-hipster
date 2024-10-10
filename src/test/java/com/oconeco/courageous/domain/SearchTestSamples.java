package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SearchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Search getSearchSample1() {
        return new Search().id(1L).query("query1");
    }

    public static Search getSearchSample2() {
        return new Search().id(2L).query("query2");
    }

    public static Search getSearchRandomSampleGenerator() {
        return new Search().id(longCount.incrementAndGet()).query(UUID.randomUUID().toString());
    }
}
