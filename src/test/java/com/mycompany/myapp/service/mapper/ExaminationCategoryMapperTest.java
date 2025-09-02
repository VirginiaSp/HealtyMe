package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ExaminationCategoryAsserts.*;
import static com.mycompany.myapp.domain.ExaminationCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExaminationCategoryMapperTest {

    private ExaminationCategoryMapper examinationCategoryMapper;

    @BeforeEach
    void setUp() {
        examinationCategoryMapper = new ExaminationCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExaminationCategorySample1();
        var actual = examinationCategoryMapper.toEntity(examinationCategoryMapper.toDto(expected));
        assertExaminationCategoryAllPropertiesEquals(expected, actual);
    }
}
