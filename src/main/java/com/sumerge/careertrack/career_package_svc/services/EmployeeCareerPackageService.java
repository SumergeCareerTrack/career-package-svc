package com.sumerge.careertrack.career_package_svc.services;

import com.sumerge.careertrack.career_package_svc.entities.EmployeeCareerPackage;
import com.sumerge.careertrack.career_package_svc.entities.enums.ApprovalStatus;
import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import com.sumerge.careertrack.career_package_svc.exceptions.DoesNotExistException;
import com.sumerge.careertrack.career_package_svc.mappers.EmployeeCareerPackageMapper;
import com.sumerge.careertrack.career_package_svc.repositories.EmployeeCareerPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeCareerPackageService {

    private final EmployeeCareerPackageRepository employeeCareerPackageRepository;
    private final EmployeeCareerPackageMapper employeeCareerPackageMapper;

    public List<EmployeeCareerPackageResponseDTO> getAllEmployeeCareerPackages() {
        List<EmployeeCareerPackage> employeeCareerPackages = employeeCareerPackageRepository.findAll();
        return employeeCareerPackages.stream().map(employeeCareerPackageMapper::toEmployeeCareerPackageResponseDTO).collect(Collectors.toList());
    }

    public EmployeeCareerPackageResponseDTO getEmployeeCareerPackageByPackageId(UUID id) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , id));

        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackage);
    }

    public EmployeeCareerPackageResponseDTO getEmployeeCareerPackageByEmployeeId(UUID id) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findByEmployeeId(id)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.Employee , id));

        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackage);
    }

    public EmployeeCareerPackageResponseDTO createEmployeeCareerPackage(EmployeeCareerPackageRequestDTO employeeCareerPackageRequestDTO) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageMapper.toEmployeeCareerPackage(employeeCareerPackageRequestDTO);

        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }

    public EmployeeCareerPackageResponseDTO updateEmployeeCareerPackage(UUID packageId,  EmployeeCareerPackageRequestDTO employeeCareerPackageRequestDTO) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId));

        employeeCareerPackage.setUrl(employeeCareerPackageRequestDTO.getUrl());
        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }

    public String deleteEmployeeCareerPackage(UUID packageId) {
        if (!employeeCareerPackageRepository.existsById(packageId)) {
            throw new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId);
        }
        else employeeCareerPackageRepository.deleteById(packageId);

        return "Deleted Successfully";
    }

    public EmployeeCareerPackageResponseDTO approveEmployeeCareerPackage(UUID packageId , String comment) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId));

        employeeCareerPackage.setApprovalStatus(ApprovalStatus.APPROVED);
        employeeCareerPackage.setComment(comment);

        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }

    public EmployeeCareerPackageResponseDTO rejectEmployeeCareerPackage(UUID packageId , String comment) {
        EmployeeCareerPackage employeeCareerPackage = employeeCareerPackageRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE , packageId));

        employeeCareerPackage.setApprovalStatus(ApprovalStatus.REJECTED);
        employeeCareerPackage.setComment(comment);

        return employeeCareerPackageMapper.toEmployeeCareerPackageResponseDTO(employeeCareerPackageRepository.save(employeeCareerPackage));
    }
}
