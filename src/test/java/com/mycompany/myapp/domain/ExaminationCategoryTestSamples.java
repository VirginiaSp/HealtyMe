package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExaminationCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExaminationCategory getExaminationCategorySample1() {
        return new ExaminationCategory().id(1L).name("name1").description("description1");
    }

    public static ExaminationCategory getExaminationCategorySample2() {
        return new ExaminationCategory().id(2L).name("name2").description("description2");
    }

    public static ExaminationCategory getExaminationCategoryRandomSampleGenerator() {
        return new ExaminationCategory()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
