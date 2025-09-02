package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AppointmentTestSamples.*;
import static com.mycompany.myapp.domain.DoctorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointment.class);
        Appointment appointment1 = getAppointmentSample1();
        Appointment appointment2 = new Appointment();
        assertThat(appointment1).isNotEqualTo(appointment2);

        appointment2.setId(appointment1.getId());
        assertThat(appointment1).isEqualTo(appointment2);

        appointment2 = getAppointmentSample2();
        assertThat(appointment1).isNotEqualTo(appointment2);
    }

    @Test
    void doctorTest() {
        Appointment appointment = getAppointmentRandomSampleGenerator();
        Doctor doctorBack = getDoctorRandomSampleGenerator();

        appointment.setDoctor(doctorBack);
        assertThat(appointment.getDoctor()).isEqualTo(doctorBack);

        appointment.doctor(null);
        assertThat(appointment.getDoctor()).isNull();
    }
}
