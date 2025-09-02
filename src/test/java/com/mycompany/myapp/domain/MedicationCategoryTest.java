package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MedicationCategoryTestSamples.*;
import static com.mycompany.myapp.domain.MedicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicationCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationCategory.class);
        MedicationCategory medicationCategory1 = getMedicationCategorySample1();
        MedicationCategory medicationCategory2 = new MedicationCategory();
        assertThat(medicationCategory1).isNotEqualTo(medicationCategory2);

        medicationCategory2.setId(medicationCategory1.getId());
        assertThat(medicationCategory1).isEqualTo(medicationCategory2);

        medicationCategory2 = getMedicationCategorySample2();
        assertThat(medicationCategory1).isNotEqualTo(medicationCategory2);
    }

    @Test
    void medicationsTest() {
        MedicationCategory medicationCategory = getMedicationCategoryRandomSampleGenerator();
        Medication medicationBack = getMedicationRandomSampleGenerator();

        medicationCategory.addMedications(medicationBack);
        assertThat(medicationCategory.getMedications()).containsOnly(medicationBack);
        assertThat(medicationBack.getCategories()).containsOnly(medicationCategory);

        medicationCategory.removeMedications(medicationBack);
        assertThat(medicationCategory.getMedications()).doesNotContain(medicationBack);
        assertThat(medicationBack.getCategories()).doesNotContain(medicationCategory);

        medicationCategory.medications(new HashSet<>(Set.of(medicationBack)));
        assertThat(medicationCategory.getMedications()).containsOnly(medicationBack);
        assertThat(medicationBack.getCategories()).containsOnly(medicationCategory);

        medicationCategory.setMedications(new HashSet<>());
        assertThat(medicationCategory.getMedications()).doesNotContain(medicationBack);
        assertThat(medicationBack.getCategories()).doesNotContain(medicationCategory);
    }
}
