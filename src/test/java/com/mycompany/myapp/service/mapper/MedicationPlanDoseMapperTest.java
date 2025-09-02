package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MedicationPlanDoseAsserts.*;
import static com.mycompany.myapp.domain.MedicationPlanDoseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicationPlanDoseMapperTest {

    private MedicationPlanDoseMapper medicationPlanDoseMapper;

    @BeforeEach
    void setUp() {
        medicationPlanDoseMapper = new MedicationPlanDoseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicationPlanDoseSample1();
        var actual = medicationPlanDoseMapper.toEntity(medicationPlanDoseMapper.toDto(expected));
        assertMedicationPlanDoseAllPropertiesEquals(expected, actual);
    }
}
