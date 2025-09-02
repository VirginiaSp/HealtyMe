package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedicationCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MedicationCategory getMedicationCategorySample1() {
        return new MedicationCategory().id(1L).name("name1").description("description1");
    }

    public static MedicationCategory getMedicationCategorySample2() {
        return new MedicationCategory().id(2L).name("name2").description("description2");
    }

    public static MedicationCategory getMedicationCategoryRandomSampleGenerator() {
        return new MedicationCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
