package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ExaminationRecordAsserts.*;
import static com.mycompany.myapp.domain.ExaminationRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExaminationRecordMapperTest {

    private ExaminationRecordMapper examinationRecordMapper;

    @BeforeEach
    void setUp() {
        examinationRecordMapper = new ExaminationRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExaminationRecordSample1();
        var actual = examinationRecordMapper.toEntity(examinationRecordMapper.toDto(expected));
        assertExaminationRecordAllPropertiesEquals(expected, actual);
    }
}
