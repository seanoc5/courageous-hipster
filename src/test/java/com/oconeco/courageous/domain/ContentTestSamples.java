package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Content getContentSample1() {
        return new Content()
            .id(1L)
            .title("title1")
            .uri("uri1")
            .path("path1")
            .source("source1")
            .params("params1")
            .textSize(1L)
            .structureSize(1L)
            .author("author1")
            .language("language1")
            .type("type1")
            .subtype("subtype1")
            .mineType("mineType1")
            .offensiveFlag("offensiveFlag1")
            .favicon("favicon1");
    }

    public static Content getContentSample2() {
        return new Content()
            .id(2L)
            .title("title2")
            .uri("uri2")
            .path("path2")
            .source("source2")
            .params("params2")
            .textSize(2L)
            .structureSize(2L)
            .author("author2")
            .language("language2")
            .type("type2")
            .subtype("subtype2")
            .mineType("mineType2")
            .offensiveFlag("offensiveFlag2")
            .favicon("favicon2");
    }

    public static Content getContentRandomSampleGenerator() {
        return new Content()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .uri(UUID.randomUUID().toString())
            .path(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString())
            .params(UUID.randomUUID().toString())
            .textSize(longCount.incrementAndGet())
            .structureSize(longCount.incrementAndGet())
            .author(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .subtype(UUID.randomUUID().toString())
            .mineType(UUID.randomUUID().toString())
            .offensiveFlag(UUID.randomUUID().toString())
            .favicon(UUID.randomUUID().toString());
    }
}
