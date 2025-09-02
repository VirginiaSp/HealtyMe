package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ExaminationCategoryTestSamples.*;
import static com.mycompany.myapp.domain.ExaminationRecordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExaminationRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExaminationRecord.class);
        ExaminationRecord examinationRecord1 = getExaminationRecordSample1();
        ExaminationRecord examinationRecord2 = new ExaminationRecord();
        assertThat(examinationRecord1).isNotEqualTo(examinationRecord2);

        examinationRecord2.setId(examinationRecord1.getId());
        assertThat(examinationRecord1).isEqualTo(examinationRecord2);

        examinationRecord2 = getExaminationRecordSample2();
        assertThat(examinationRecord1).isNotEqualTo(examinationRecord2);
    }

    @Test
    void categoryTest() {
        ExaminationRecord examinationRecord = getExaminationRecordRandomSampleGenerator();
        ExaminationCategory examinationCategoryBack = getExaminationCategoryRandomSampleGenerator();

        examinationRecord.setCategory(examinationCategoryBack);
        assertThat(examinationRecord.getCategory()).isEqualTo(examinationCategoryBack);

        examinationRecord.category(null);
        assertThat(examinationRecord.getCategory()).isNull();
    }
}
