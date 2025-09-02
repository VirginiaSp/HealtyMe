package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationPlanDoseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationPlanDoseDTO.class);
        MedicationPlanDoseDTO medicationPlanDoseDTO1 = new MedicationPlanDoseDTO();
        medicationPlanDoseDTO1.setId(1L);
        MedicationPlanDoseDTO medicationPlanDoseDTO2 = new MedicationPlanDoseDTO();
        assertThat(medicationPlanDoseDTO1).isNotEqualTo(medicationPlanDoseDTO2);
        medicationPlanDoseDTO2.setId(medicationPlanDoseDTO1.getId());
        assertThat(medicationPlanDoseDTO1).isEqualTo(medicationPlanDoseDTO2);
        medicationPlanDoseDTO2.setId(2L);
        assertThat(medicationPlanDoseDTO1).isNotEqualTo(medicationPlanDoseDTO2);
        medicationPlanDoseDTO1.setId(null);
        assertThat(medicationPlanDoseDTO1).isNotEqualTo(medicationPlanDoseDTO2);
    }
}
