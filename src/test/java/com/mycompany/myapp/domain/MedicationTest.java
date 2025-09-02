package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MedicationCategoryTestSamples.*;
import static com.mycompany.myapp.domain.MedicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medication.class);
        Medication medication1 = getMedicationSample1();
        Medication medication2 = new Medication();
        assertThat(medication1).isNotEqualTo(medication2);

        medication2.setId(medication1.getId());
        assertThat(medication1).isEqualTo(medication2);

        medication2 = getMedicationSample2();
        assertThat(medication1).isNotEqualTo(medication2);
    }

    @Test
    void categoriesTest() {
        Medication medication = getMedicationRandomSampleGenerator();
        MedicationCategory medicationCategoryBack = getMedicationCategoryRandomSampleGenerator();

        medication.addCategories(medicationCategoryBack);
        assertThat(medication.getCategories()).containsOnly(medicationCategoryBack);

        medication.removeCategories(medicationCategoryBack);
        assertThat(medication.getCategories()).doesNotContain(medicationCategoryBack);

        medication.categories(new HashSet<>(Set.of(medicationCategoryBack)));
        assertThat(medication.getCategories()).containsOnly(medicationCategoryBack);

        medication.setCategories(new HashSet<>());
        assertThat(medication.getCategories()).doesNotContain(medicationCategoryBack);
    }
}
