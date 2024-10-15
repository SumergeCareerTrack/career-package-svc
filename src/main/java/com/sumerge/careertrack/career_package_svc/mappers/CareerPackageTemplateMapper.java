package com.sumerge.careertrack.career_package_svc.mappers;

import com.sumerge.careertrack.career_package_svc.entities.CareerPackageTemplate;
import com.sumerge.careertrack.career_package_svc.entities.requests.CareerPackageTemplateRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.CareerPackageTemplateResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CareerPackageTemplateMapper {

    @Mapping(target = "fileId" , ignore = true)
    @Mapping(source = "titleId" , target = "titleId")
    @Mapping(source = "name", target = "name")
    @Mapping(target = "id" , ignore = true)
    CareerPackageTemplate toCarerPackageTemplate(CareerPackageTemplateRequestDTO templateRequestDTO);

    @Mapping(source = "fileId" , target = "fileId")
    @Mapping(source = "titleId" , target = "titleId")
    @Mapping(source = "name" , target = "name")
    @Mapping(source = "id" ,target = "id")
    CareerPackageTemplateResponseDTO toResponseDTO(CareerPackageTemplate template);
}
