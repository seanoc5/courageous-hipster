package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContentFragmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContentFragment getContentFragmentSample1() {
        return new ContentFragment()
            .id(1L)
            .label("label1")
            .startPos(1L)
            .endPos(1L)
            .startTermNum(1L)
            .endTermNum(1L)
            .subtype("subtype1")
            .language("language1");
    }

    public static ContentFragment getContentFragmentSample2() {
        return new ContentFragment()
            .id(2L)
            .label("label2")
            .startPos(2L)
            .endPos(2L)
            .startTermNum(2L)
            .endTermNum(2L)
            .subtype("subtype2")
            .language("language2");
    }

    public static ContentFragment getContentFragmentRandomSampleGenerator() {
        return new ContentFragment()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .startPos(longCount.incrementAndGet())
            .endPos(longCount.incrementAndGet())
            .startTermNum(longCount.incrementAndGet())
            .endTermNum(longCount.incrementAndGet())
            .subtype(UUID.randomUUID().toString())
            .language(UUID.randomUUID().toString());
    }
}
