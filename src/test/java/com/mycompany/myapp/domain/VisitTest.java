package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DoctorTestSamples.*;
import static com.mycompany.myapp.domain.VisitTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visit.class);
        Visit visit1 = getVisitSample1();
        Visit visit2 = new Visit();
        assertThat(visit1).isNotEqualTo(visit2);

        visit2.setId(visit1.getId());
        assertThat(visit1).isEqualTo(visit2);

        visit2 = getVisitSample2();
        assertThat(visit1).isNotEqualTo(visit2);
    }

    @Test
    void doctorTest() {
        Visit visit = getVisitRandomSampleGenerator();
        Doctor doctorBack = getDoctorRandomSampleGenerator();

        visit.setDoctor(doctorBack);
        assertThat(visit.getDoctor()).isEqualTo(doctorBack);

        visit.doctor(null);
        assertThat(visit.getDoctor()).isNull();
    }
}
