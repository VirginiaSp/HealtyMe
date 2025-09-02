package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MedicationCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedicationCategoryDTO.class);
        MedicationCategoryDTO medicationCategoryDTO1 = new MedicationCategoryDTO();
        medicationCategoryDTO1.setId(1L);
        MedicationCategoryDTO medicationCategoryDTO2 = new MedicationCategoryDTO();
        assertThat(medicationCategoryDTO1).isNotEqualTo(medicationCategoryDTO2);
        medicationCategoryDTO2.setId(medicationCategoryDTO1.getId());
        assertThat(medicationCategoryDTO1).isEqualTo(medicationCategoryDTO2);
        medicationCategoryDTO2.setId(2L);
        assertThat(medicationCategoryDTO1).isNotEqualTo(medicationCategoryDTO2);
        medicationCategoryDTO1.setId(null);
        assertThat(medicationCategoryDTO1).isNotEqualTo(medicationCategoryDTO2);
    }
}
