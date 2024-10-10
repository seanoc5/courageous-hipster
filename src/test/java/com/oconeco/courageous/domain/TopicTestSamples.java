package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TopicTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Topic getTopicSample1() {
        return new Topic().id(1L).label("label1").description("description1").level(1);
    }

    public static Topic getTopicSample2() {
        return new Topic().id(2L).label("label2").description("description2").level(2);
    }

    public static Topic getTopicRandomSampleGenerator() {
        return new Topic()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .level(intCount.incrementAndGet());
    }
}
