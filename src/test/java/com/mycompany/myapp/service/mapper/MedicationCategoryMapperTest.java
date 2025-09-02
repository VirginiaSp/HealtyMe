package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.MedicationCategoryAsserts.*;
import static com.mycompany.myapp.domain.MedicationCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicationCategoryMapperTest {

    private MedicationCategoryMapper medicationCategoryMapper;

    @BeforeEach
    void setUp() {
        medicationCategoryMapper = new MedicationCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedicationCategorySample1();
        var actual = medicationCategoryMapper.toEntity(medicationCategoryMapper.toDto(expected));
        assertMedicationCategoryAllPropertiesEquals(expected, actual);
    }
}
