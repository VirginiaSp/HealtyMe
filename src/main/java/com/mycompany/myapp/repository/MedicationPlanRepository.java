package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MedicationPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicationPlan entity.
 */
@Repository
public interface MedicationPlanRepository extends JpaRepository<MedicationPlan, Long>, JpaSpecificationExecutor<MedicationPlan> {
    @Query("select medicationPlan from MedicationPlan medicationPlan where medicationPlan.owner.login = ?#{authentication.name}")
    List<MedicationPlan> findByOwnerIsCurrentUser();

    default Optional<MedicationPlan> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicationPlan> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicationPlan> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicationPlan from MedicationPlan medicationPlan left join fetch medicationPlan.owner",
        countQuery = "select count(medicationPlan) from MedicationPlan medicationPlan"
    )
    Page<MedicationPlan> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicationPlan from MedicationPlan medicationPlan left join fetch medicationPlan.owner")
    List<MedicationPlan> findAllWithToOneRelationships();

    @Query("select medicationPlan from MedicationPlan medicationPlan left join fetch medicationPlan.owner where medicationPlan.id =:id")
    Optional<MedicationPlan> findOneWithToOneRelationships(@Param("id") Long id);
}
