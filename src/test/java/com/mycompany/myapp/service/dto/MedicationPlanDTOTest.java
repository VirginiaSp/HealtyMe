package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationPlanDTO.class);
        MedicationPlanDTO medicationPlanDTO1 = new MedicationPlanDTO();
        medicationPlanDTO1.setId(1L);
        MedicationPlanDTO medicationPlanDTO2 = new MedicationPlanDTO();
        assertThat(medicationPlanDTO1).isNotEqualTo(medicationPlanDTO2);
        medicationPlanDTO2.setId(medicationPlanDTO1.getId());
        assertThat(medicationPlanDTO1).isEqualTo(medicationPlanDTO2);
        medicationPlanDTO2.setId(2L);
        assertThat(medicationPlanDTO1).isNotEqualTo(medicationPlanDTO2);
        medicationPlanDTO1.setId(null);
        assertThat(medicationPlanDTO1).isNotEqualTo(medicationPlanDTO2);
    }
}
