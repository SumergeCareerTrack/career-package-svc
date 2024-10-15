package com.sumerge.careertrack.career_package_svc.repositories;

import com.sumerge.careertrack.career_package_svc.entities.EmployeeCareerPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeCareerPackageRepository extends JpaRepository<EmployeeCareerPackage , UUID> {
    Optional<EmployeeCareerPackage> findByEmployeeId(UUID employeeId);
    List<EmployeeCareerPackage> findAllByEmployeeId(UUID employeeId);
    List<EmployeeCareerPackage> findAllByEmployeeIdOrderBySubmissionDateAsc(UUID employeeId);

    @Query("SELECT e FROM EmployeeCareerPackage e WHERE e.employeeId IN :employeeIds ORDER BY e.submissionDate DESC")
    List<EmployeeCareerPackage> findLatestSubmissionsByEmployeeIds(@Param("employeeIds") List<UUID> employeeIds);

}
