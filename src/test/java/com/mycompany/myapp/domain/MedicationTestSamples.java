package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Medication getMedicationSample1() {
        return new Medication().id(1L).name("name1").rating(1).notes("notes1");
    }

    public static Medication getMedicationSample2() {
        return new Medication().id(2L).name("name2").rating(2).notes("notes2");
    }

    public static Medication getMedicationRandomSampleGenerator() {
        return new Medication()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .rating(intCount.incrementAndGet())
            .notes(UUID.randomUUID().toString());
    }
}
