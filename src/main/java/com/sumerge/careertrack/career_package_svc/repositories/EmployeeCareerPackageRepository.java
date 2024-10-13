package com.sumerge.careertrack.career_package_svc.repositories;

import com.sumerge.careertrack.career_package_svc.entities.EmployeeCareerPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeCareerPackageRepository extends JpaRepository<EmployeeCareerPackage , UUID> {
    Optional<EmployeeCareerPackage> findByEmployeeId(UUID employeeId);
}
