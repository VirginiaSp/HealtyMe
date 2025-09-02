package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ExaminationCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExaminationCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationCategory.class);
        ExaminationCategory examinationCategory1 = getExaminationCategorySample1();
        ExaminationCategory examinationCategory2 = new ExaminationCategory();
        assertThat(examinationCategory1).isNotEqualTo(examinationCategory2);

        examinationCategory2.setId(examinationCategory1.getId());
        assertThat(examinationCategory1).isEqualTo(examinationCategory2);

        examinationCategory2 = getExaminationCategorySample2();
        assertThat(examinationCategory1).isNotEqualTo(examinationCategory2);
    }
}
