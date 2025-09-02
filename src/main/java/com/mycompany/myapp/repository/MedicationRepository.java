package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medication entity.
 *
 * When extending this class, extend MedicationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MedicationRepository
    extends MedicationRepositoryWithBagRelationships, JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication> {
    @Query("select medication from Medication medication where medication.owner.login = ?#{authentication.name}")
    List<Medication> findByOwnerIsCurrentUser();

    default Optional<Medication> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Medication> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Medication> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select medication from Medication medication left join fetch medication.owner",
        countQuery = "select count(medication) from Medication medication"
    )
    Page<Medication> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medication from Medication medication left join fetch medication.owner")
    List<Medication> findAllWithToOneRelationships();

    @Query("select medication from Medication medication left join fetch medication.owner where medication.id =:id")
    Optional<Medication> findOneWithToOneRelationships(@Param("id") Long id);
}
