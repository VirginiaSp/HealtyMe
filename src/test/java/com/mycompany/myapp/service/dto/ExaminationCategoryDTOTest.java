package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExaminationCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationCategoryDTO.class);
        ExaminationCategoryDTO examinationCategoryDTO1 = new ExaminationCategoryDTO();
        examinationCategoryDTO1.setId(1L);
        ExaminationCategoryDTO examinationCategoryDTO2 = new ExaminationCategoryDTO();
        assertThat(examinationCategoryDTO1).isNotEqualTo(examinationCategoryDTO2);
        examinationCategoryDTO2.setId(examinationCategoryDTO1.getId());
        assertThat(examinationCategoryDTO1).isEqualTo(examinationCategoryDTO2);
        examinationCategoryDTO2.setId(2L);
        assertThat(examinationCategoryDTO1).isNotEqualTo(examinationCategoryDTO2);
        examinationCategoryDTO1.setId(null);
        assertThat(examinationCategoryDTO1).isNotEqualTo(examinationCategoryDTO2);
    }
}
