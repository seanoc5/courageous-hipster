package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnalyzerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Analyzer getAnalyzerSample1() {
        return new Analyzer().id(1L).label("label1").code("code1");
    }

    public static Analyzer getAnalyzerSample2() {
        return new Analyzer().id(2L).label("label2").code("code2");
    }

    public static Analyzer getAnalyzerRandomSampleGenerator() {
        return new Analyzer().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString()).code(UUID.randomUUID().toString());
    }
}
