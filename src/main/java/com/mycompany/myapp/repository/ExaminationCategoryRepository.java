package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExaminationCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExaminationCategory entity.
 */
@Repository
public interface ExaminationCategoryRepository
    extends JpaRepository<ExaminationCategory, Long>, JpaSpecificationExecutor<ExaminationCategory> {
    @Query(
        "select examinationCategory from ExaminationCategory examinationCategory where examinationCategory.owner.login = ?#{authentication.name}"
    )
    List<ExaminationCategory> findByOwnerIsCurrentUser();

    default Optional<ExaminationCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ExaminationCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ExaminationCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select examinationCategory from ExaminationCategory examinationCategory left join fetch examinationCategory.owner",
        countQuery = "select count(examinationCategory) from ExaminationCategory examinationCategory"
    )
    Page<ExaminationCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select examinationCategory from ExaminationCategory examinationCategory left join fetch examinationCategory.owner")
    List<ExaminationCategory> findAllWithToOneRelationships();

    @Query(
        "select examinationCategory from ExaminationCategory examinationCategory left join fetch examinationCategory.owner where examinationCategory.id =:id"
    )
    Optional<ExaminationCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
