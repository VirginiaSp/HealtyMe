package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExaminationRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExaminationRecord entity.
 */
@Repository
public interface ExaminationRecordRepository extends JpaRepository<ExaminationRecord, Long>, JpaSpecificationExecutor<ExaminationRecord> {
    @Query(
        "select examinationRecord from ExaminationRecord examinationRecord where examinationRecord.owner.login = ?#{authentication.name}"
    )
    List<ExaminationRecord> findByOwnerIsCurrentUser();

    default Optional<ExaminationRecord> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ExaminationRecord> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ExaminationRecord> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select examinationRecord from ExaminationRecord examinationRecord left join fetch examinationRecord.owner",
        countQuery = "select count(examinationRecord) from ExaminationRecord examinationRecord"
    )
    Page<ExaminationRecord> findAllWithToOneRelationships(Pageable pageable);

    @Query("select examinationRecord from ExaminationRecord examinationRecord left join fetch examinationRecord.owner")
    List<ExaminationRecord> findAllWithToOneRelationships();

    @Query(
        "select examinationRecord from ExaminationRecord examinationRecord left join fetch examinationRecord.owner where examinationRecord.id =:id"
    )
    Optional<ExaminationRecord> findOneWithToOneRelationships(@Param("id") Long id);
}
