package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DoctorVisit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorVisitRepository extends JpaRepository<DoctorVisit, Long> {
    List<DoctorVisit> findByDoctorId(Long doctorId);

    // Add this method for secure update/delete operations
    Optional<DoctorVisit> findByIdAndDoctorId(Long id, Long doctorId);
}
