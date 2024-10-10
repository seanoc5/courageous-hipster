package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContextTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Context getContextSample1() {
        return new Context()
            .id(1L)
            .label("label1")
            .description("description1")
            .level("level1")
            .time("time1")
            .location("location1")
            .intent("intent1");
    }

    public static Context getContextSample2() {
        return new Context()
            .id(2L)
            .label("label2")
            .description("description2")
            .level("level2")
            .time("time2")
            .location("location2")
            .intent("intent2");
    }

    public static Context getContextRandomSampleGenerator() {
        return new Context()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .level(UUID.randomUUID().toString())
            .time(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .intent(UUID.randomUUID().toString());
    }
}
