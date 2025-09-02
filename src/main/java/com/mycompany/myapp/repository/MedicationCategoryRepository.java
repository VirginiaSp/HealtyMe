package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MedicationCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MedicationCategory entity.
 */
@Repository
public interface MedicationCategoryRepository
    extends JpaRepository<MedicationCategory, Long>, JpaSpecificationExecutor<MedicationCategory> {
    @Query(
        "select medicationCategory from MedicationCategory medicationCategory where medicationCategory.owner.login = ?#{authentication.name}"
    )
    List<MedicationCategory> findByOwnerIsCurrentUser();

    default Optional<MedicationCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedicationCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedicationCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medicationCategory from MedicationCategory medicationCategory left join fetch medicationCategory.owner",
        countQuery = "select count(medicationCategory) from MedicationCategory medicationCategory"
    )
    Page<MedicationCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medicationCategory from MedicationCategory medicationCategory left join fetch medicationCategory.owner")
    List<MedicationCategory> findAllWithToOneRelationships();

    @Query(
        "select medicationCategory from MedicationCategory medicationCategory left join fetch medicationCategory.owner where medicationCategory.id =:id"
    )
    Optional<MedicationCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
