package com.sumerge.careertrack.career_package_svc.mappers;

import com.sumerge.careertrack.career_package_svc.entities.CareerPackageTemplate;
import com.sumerge.careertrack.career_package_svc.entities.requests.CareerPackageTemplateRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.CareerPackageTemplateResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CareerPackageTemplateMapper {

    @Mapping(source = "url" , target = "url")
    @Mapping(source = "titleId" , target = "titleId")
    @Mapping(target = "id" , ignore = true)
    CareerPackageTemplate toCarerPackageTemplate(CareerPackageTemplateRequestDTO templateRequestDTO);

    @Mapping(source = "url" , target = "url")
    @Mapping(source = "titleId" , target = "titleId")
    @Mapping(source = "id" ,target = "id")
    CareerPackageTemplateResponseDTO toResponseDTO(CareerPackageTemplate template);
}
