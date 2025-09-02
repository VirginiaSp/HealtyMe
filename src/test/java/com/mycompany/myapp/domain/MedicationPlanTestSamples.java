package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicationPlan getMedicationPlanSample1() {
        return new MedicationPlan().id(1L).planName("planName1").medicationName("medicationName1").colorHex("colorHex1");
    }

    public static MedicationPlan getMedicationPlanSample2() {
        return new MedicationPlan().id(2L).planName("planName2").medicationName("medicationName2").colorHex("colorHex2");
    }

    public static MedicationPlan getMedicationPlanRandomSampleGenerator() {
        return new MedicationPlan()
            .id(longCount.incrementAndGet())
            .planName(UUID.randomUUID().toString())
            .medicationName(UUID.randomUUID().toString())
            .colorHex(UUID.randomUUID().toString());
    }
}
