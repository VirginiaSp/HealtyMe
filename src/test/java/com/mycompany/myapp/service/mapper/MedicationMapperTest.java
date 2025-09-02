package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MedicationAsserts.*;
import static com.mycompany.myapp.domain.MedicationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicationMapperTest {

    private MedicationMapper medicationMapper;

    @BeforeEach
    void setUp() {
        medicationMapper = new MedicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicationSample1();
        var actual = medicationMapper.toEntity(medicationMapper.toDto(expected));
        assertMedicationAllPropertiesEquals(expected, actual);
    }
}
