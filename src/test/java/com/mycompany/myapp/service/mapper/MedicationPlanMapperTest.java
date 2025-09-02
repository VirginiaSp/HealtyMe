package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MedicationPlanAsserts.*;
import static com.mycompany.myapp.domain.MedicationPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicationPlanMapperTest {

    private MedicationPlanMapper medicationPlanMapper;

    @BeforeEach
    void setUp() {
        medicationPlanMapper = new MedicationPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicationPlanSample1();
        var actual = medicationPlanMapper.toEntity(medicationPlanMapper.toDto(expected));
        assertMedicationPlanAllPropertiesEquals(expected, actual);
    }
}
