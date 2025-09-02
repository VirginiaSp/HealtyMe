package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExaminationRecordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExaminationRecord getExaminationRecordSample1() {
        return new ExaminationRecord()
            .id(1L)
            .title("title1")
            .originalFilename("originalFilename1")
            .storedFilename("storedFilename1")
            .notes("notes1");
    }

    public static ExaminationRecord getExaminationRecordSample2() {
        return new ExaminationRecord()
            .id(2L)
            .title("title2")
            .originalFilename("originalFilename2")
            .storedFilename("storedFilename2")
            .notes("notes2");
    }

    public static ExaminationRecord getExaminationRecordRandomSampleGenerator() {
        return new ExaminationRecord()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .originalFilename(UUID.randomUUID().toString())
            .storedFilename(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
