package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository
    extends MedicationRepositoryWithBagRelationships, JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication> {
    @Query("select medication from Medication medication where medication.owner.login = ?#{authentication.name}")
    List<Medication> findByOwnerIsCurrentUser();

    default Optional<Medication> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Medication> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Medication> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
