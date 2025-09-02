package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MedicationPlanDoseTestSamples.*;
import static com.mycompany.myapp.domain.MedicationPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicationPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationPlan.class);
        MedicationPlan medicationPlan1 = getMedicationPlanSample1();
        MedicationPlan medicationPlan2 = new MedicationPlan();
        assertThat(medicationPlan1).isNotEqualTo(medicationPlan2);

        medicationPlan2.setId(medicationPlan1.getId());
        assertThat(medicationPlan1).isEqualTo(medicationPlan2);

        medicationPlan2 = getMedicationPlanSample2();
        assertThat(medicationPlan1).isNotEqualTo(medicationPlan2);
    }

    @Test
    void dosesTest() {
        MedicationPlan medicationPlan = getMedicationPlanRandomSampleGenerator();
        MedicationPlanDose medicationPlanDoseBack = getMedicationPlanDoseRandomSampleGenerator();

        medicationPlan.addDoses(medicationPlanDoseBack);
        assertThat(medicationPlan.getDoses()).containsOnly(medicationPlanDoseBack);
        assertThat(medicationPlanDoseBack.getPlan()).isEqualTo(medicationPlan);

        medicationPlan.removeDoses(medicationPlanDoseBack);
        assertThat(medicationPlan.getDoses()).doesNotContain(medicationPlanDoseBack);
        assertThat(medicationPlanDoseBack.getPlan()).isNull();

        medicationPlan.doses(new HashSet<>(Set.of(medicationPlanDoseBack)));
        assertThat(medicationPlan.getDoses()).containsOnly(medicationPlanDoseBack);
        assertThat(medicationPlanDoseBack.getPlan()).isEqualTo(medicationPlan);

        medicationPlan.setDoses(new HashSet<>());
        assertThat(medicationPlan.getDoses()).doesNotContain(medicationPlanDoseBack);
        assertThat(medicationPlanDoseBack.getPlan()).isNull();
    }
}
