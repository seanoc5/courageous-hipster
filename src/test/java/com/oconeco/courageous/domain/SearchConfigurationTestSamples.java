package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SearchConfigurationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SearchConfiguration getSearchConfigurationSample1() {
        return new SearchConfiguration().id(1L).label("label1").description("description1").url("url1");
    }

    public static SearchConfiguration getSearchConfigurationSample2() {
        return new SearchConfiguration().id(2L).label("label2").description("description2").url("url2");
    }

    public static SearchConfiguration getSearchConfigurationRandomSampleGenerator() {
        return new SearchConfiguration()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
