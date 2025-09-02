package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DoctorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Doctor getDoctorSample1() {
        return new Doctor()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .specialty("specialty1")
            .phone("phone1")
            .address("address1")
            .notes("notes1");
    }

    public static Doctor getDoctorSample2() {
        return new Doctor()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .specialty("specialty2")
            .phone("phone2")
            .address("address2")
            .notes("notes2");
    }

    public static Doctor getDoctorRandomSampleGenerator() {
        return new Doctor()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .specialty(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}
