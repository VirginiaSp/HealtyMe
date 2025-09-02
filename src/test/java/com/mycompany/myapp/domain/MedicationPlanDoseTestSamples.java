package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationPlanDoseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicationPlanDose getMedicationPlanDoseSample1() {
        return new MedicationPlanDose().id(1L).timeOfDay("timeOfDay1").notes("notes1");
    }

    public static MedicationPlanDose getMedicationPlanDoseSample2() {
        return new MedicationPlanDose().id(2L).timeOfDay("timeOfDay2").notes("notes2");
    }

    public static MedicationPlanDose getMedicationPlanDoseRandomSampleGenerator() {
        return new MedicationPlanDose()
            .id(longCount.incrementAndGet())
            .timeOfDay(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
