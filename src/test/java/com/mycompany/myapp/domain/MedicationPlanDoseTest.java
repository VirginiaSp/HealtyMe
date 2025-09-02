package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MedicationPlanDoseTestSamples.*;
import static com.mycompany.myapp.domain.MedicationPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationPlanDoseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationPlanDose.class);
        MedicationPlanDose medicationPlanDose1 = getMedicationPlanDoseSample1();
        MedicationPlanDose medicationPlanDose2 = new MedicationPlanDose();
        assertThat(medicationPlanDose1).isNotEqualTo(medicationPlanDose2);

        medicationPlanDose2.setId(medicationPlanDose1.getId());
        assertThat(medicationPlanDose1).isEqualTo(medicationPlanDose2);

        medicationPlanDose2 = getMedicationPlanDoseSample2();
        assertThat(medicationPlanDose1).isNotEqualTo(medicationPlanDose2);
    }

    @Test
    void planTest() {
        MedicationPlanDose medicationPlanDose = getMedicationPlanDoseRandomSampleGenerator();
        MedicationPlan medicationPlanBack = getMedicationPlanRandomSampleGenerator();

        medicationPlanDose.setPlan(medicationPlanBack);
        assertThat(medicationPlanDose.getPlan()).isEqualTo(medicationPlanBack);

        medicationPlanDose.plan(null);
        assertThat(medicationPlanDose.getPlan()).isNull();
    }
}
