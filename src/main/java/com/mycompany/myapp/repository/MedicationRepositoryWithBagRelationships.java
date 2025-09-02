package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MedicationRepositoryWithBagRelationships {
    Optional<Medication> fetchBagRelationships(Optional<Medication> medication);

    List<Medication> fetchBagRelationships(List<Medication> medications);

    Page<Medication> fetchBagRelationships(Page<Medication> medications);
}
