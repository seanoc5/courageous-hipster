package com.oconeco.courageous.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ThingTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ThingType getThingTypeSample1() {
        return new ThingType().id(1L).label("label1").parentClass("parentClass1").descrption("descrption1");
    }

    public static ThingType getThingTypeSample2() {
        return new ThingType().id(2L).label("label2").parentClass("parentClass2").descrption("descrption2");
    }

    public static ThingType getThingTypeRandomSampleGenerator() {
        return new ThingType()
            .id(longCount.incrementAndGet())
            .label(UUID.randomUUID().toString())
            .parentClass(UUID.randomUUID().toString())
            .descrption(UUID.randomUUID().toString());
    }
}
