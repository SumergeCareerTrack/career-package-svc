package com.sumerge.careertrack.career_package_svc.mappers;

import com.sumerge.careertrack.career_package_svc.entities.EmployeeCareerPackage;
import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeCareerPackageMapper {

    @Mapping(target = "id" , ignore = true)
    @Mapping(source = "employeeId", target = "employeeId")
    @Mapping(source = "url", target = "url")
    @Mapping(target = "comment" , ignore = true)
    @Mapping(target = "approvalStatus" , ignore = true)
    EmployeeCareerPackage toEmployeeCareerPackage(EmployeeCareerPackageRequestDTO employeeCareerPackageRequestDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "employeeId", target = "employeeId")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "approvalStatus", target = "approvalStatus")
    EmployeeCareerPackageResponseDTO toEmployeeCareerPackageResponseDTO(EmployeeCareerPackage employeeCareerPackage);
}
