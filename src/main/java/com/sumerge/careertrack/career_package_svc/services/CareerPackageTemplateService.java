package com.sumerge.careertrack.career_package_svc.services;

import com.sumerge.careertrack.career_package_svc.entities.CareerPackageTemplate;
import com.sumerge.careertrack.career_package_svc.entities.requests.CareerPackageTemplateRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.CareerPackageTemplateResponseDTO;
import com.sumerge.careertrack.career_package_svc.exceptions.AlreadyExistException;
import com.sumerge.careertrack.career_package_svc.exceptions.DoesNotExistException;
import com.sumerge.careertrack.career_package_svc.mappers.CareerPackageTemplateMapper;
import com.sumerge.careertrack.career_package_svc.repositories.CareerPackageTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerPackageTemplateService {

    private final CareerPackageTemplateRepository careerPackageTemplateRepository;
    private final CareerPackageTemplateMapper careerPackageTemplateMapper;

    public List<CareerPackageTemplateResponseDTO> getAllCareerPackages() {
        List<CareerPackageTemplate> careerPackages = careerPackageTemplateRepository.findAll();
        return careerPackages.stream().map(careerPackageTemplateMapper::toResponseDTO).collect(Collectors.toList());
    }

    public CareerPackageTemplateResponseDTO findCareerPackageById(UUID id) {
        CareerPackageTemplate careerPackage = careerPackageTemplateRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistException( DoesNotExistException.CAREER_PACKAGE , id));
        return careerPackageTemplateMapper.toResponseDTO(careerPackage);
    }

    public CareerPackageTemplateResponseDTO findCareerPackageByTitleId(UUID titleId) {
        CareerPackageTemplate careerPackage = careerPackageTemplateRepository.findByTitleId(titleId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.Title , titleId));
        return careerPackageTemplateMapper.toResponseDTO(careerPackage);
    }

    public CareerPackageTemplateResponseDTO createCareerPackage(CareerPackageTemplateRequestDTO requestDTO) {
        CareerPackageTemplate template = careerPackageTemplateMapper.toCarerPackageTemplate(requestDTO);
        if(careerPackageTemplateRepository.existsByTitleId(template.getTitleId())){
            throw new AlreadyExistException(AlreadyExistException.Title , template.getTitleId());
        }
        return careerPackageTemplateMapper.toResponseDTO(careerPackageTemplateRepository.save(template));
    }

    public CareerPackageTemplateResponseDTO updateCareerPackage( UUID packageId,CareerPackageTemplateRequestDTO requestDTO) {
        CareerPackageTemplate template = careerPackageTemplateRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.CAREER_PACKAGE, packageId));

        if(careerPackageTemplateRepository.existsByTitleId(requestDTO.getTitleId())){
            throw new AlreadyExistException(AlreadyExistException.Title , requestDTO.getTitleId());
        }

        template.setUrl(requestDTO.getUrl());
        template.setTitleId(requestDTO.getTitleId());

        return careerPackageTemplateMapper.toResponseDTO(careerPackageTemplateRepository.save(template));
    }

    public String deleteCareerPackage(UUID packageId) {
        CareerPackageTemplate template = careerPackageTemplateRepository.findById(packageId)
                .orElseThrow(() -> new DoesNotExistException(DoesNotExistException.Title , packageId));

        careerPackageTemplateRepository.delete(template);
        return "Deleted Successfully";
    }

}
