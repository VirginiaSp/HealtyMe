package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExaminationRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationRecordDTO.class);
        ExaminationRecordDTO examinationRecordDTO1 = new ExaminationRecordDTO();
        examinationRecordDTO1.setId(1L);
        ExaminationRecordDTO examinationRecordDTO2 = new ExaminationRecordDTO();
        assertThat(examinationRecordDTO1).isNotEqualTo(examinationRecordDTO2);
        examinationRecordDTO2.setId(examinationRecordDTO1.getId());
        assertThat(examinationRecordDTO1).isEqualTo(examinationRecordDTO2);
        examinationRecordDTO2.setId(2L);
        assertThat(examinationRecordDTO1).isNotEqualTo(examinationRecordDTO2);
        examinationRecordDTO1.setId(null);
        assertThat(examinationRecordDTO1).isNotEqualTo(examinationRecordDTO2);
    }
}
