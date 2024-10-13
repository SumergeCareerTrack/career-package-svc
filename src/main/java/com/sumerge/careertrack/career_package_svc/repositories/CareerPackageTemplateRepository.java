package com.sumerge.careertrack.career_package_svc.repositories;

import com.sumerge.careertrack.career_package_svc.entities.CareerPackageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CareerPackageTemplateRepository extends JpaRepository<CareerPackageTemplate, UUID> {
    Optional<CareerPackageTemplate> findByTitleId(UUID titleId);
    Boolean existsByTitleId(UUID titleId);
}
