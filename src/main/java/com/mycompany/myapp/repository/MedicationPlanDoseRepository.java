package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MedicationPlanDose;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicationPlanDose entity.
 */
@Repository
public interface MedicationPlanDoseRepository
    extends JpaRepository<MedicationPlanDose, Long>, JpaSpecificationExecutor<MedicationPlanDose> {
    @Query(
        "select medicationPlanDose from MedicationPlanDose medicationPlanDose where medicationPlanDose.owner.login = ?#{authentication.name}"
    )
    List<MedicationPlanDose> findByOwnerIsCurrentUser();

    default Optional<MedicationPlanDose> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicationPlanDose> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicationPlanDose> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicationPlanDose from MedicationPlanDose medicationPlanDose left join fetch medicationPlanDose.owner",
        countQuery = "select count(medicationPlanDose) from MedicationPlanDose medicationPlanDose"
    )
    Page<MedicationPlanDose> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicationPlanDose from MedicationPlanDose medicationPlanDose left join fetch medicationPlanDose.owner")
    List<MedicationPlanDose> findAllWithToOneRelationships();

    @Query(
        "select medicationPlanDose from MedicationPlanDose medicationPlanDose left join fetch medicationPlanDose.owner where medicationPlanDose.id =:id"
    )
    Optional<MedicationPlanDose> findOneWithToOneRelationships(@Param("id") Long id);
}
