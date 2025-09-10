package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MedicationCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationCategoryRepository
    extends JpaRepository<MedicationCategory, Long>, JpaSpecificationExecutor<MedicationCategory> {
    @Query(
        "select medicationCategory from MedicationCategory medicationCategory where medicationCategory.createdBy.login = ?#{authentication.name}"
    )
    List<MedicationCategory> findByCreatedByIsCurrentUser();
}
