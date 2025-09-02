package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Doctor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    @Query("select doctor from Doctor doctor where doctor.owner.login = ?#{authentication.name}")
    List<Doctor> findByOwnerIsCurrentUser();

    default Optional<Doctor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Doctor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Doctor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select doctor from Doctor doctor left join fetch doctor.owner", countQuery = "select count(doctor) from Doctor doctor")
    Page<Doctor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select doctor from Doctor doctor left join fetch doctor.owner")
    List<Doctor> findAllWithToOneRelationships();

    @Query("select doctor from Doctor doctor left join fetch doctor.owner where doctor.id =:id")
    Optional<Doctor> findOneWithToOneRelationships(@Param("id") Long id);
}
