package com.sumerge.careertrack.career_package_svc.controllers;

import com.sumerge.careertrack.career_package_svc.entities.requests.CareerPackageTemplateRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.CareerPackageTemplateResponseDTO;
import com.sumerge.careertrack.career_package_svc.services.CareerPackageTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/career-packages")
@RequiredArgsConstructor
public class CareerPackageTemplateController {


    private final CareerPackageTemplateService careerPackageTemplateService;

    @GetMapping
    public ResponseEntity<List<CareerPackageTemplateResponseDTO>> getAll() {
        return ResponseEntity.ok(careerPackageTemplateService.getAllCareerPackages());
    }

    @GetMapping("/{packageId}")
    public ResponseEntity<CareerPackageTemplateResponseDTO> getCareerPackageById(@PathVariable UUID packageId) {
        return ResponseEntity.ok(careerPackageTemplateService.findCareerPackageById(packageId));
    }

    @GetMapping("/title/{titleId}")
    public ResponseEntity<CareerPackageTemplateResponseDTO> getCareerPackageByTitleId(@PathVariable UUID titleId) {
        return ResponseEntity.ok(careerPackageTemplateService.findCareerPackageByTitleId(titleId));
    }

    @PostMapping
    public ResponseEntity<CareerPackageTemplateResponseDTO> createCareerPackage(@RequestParam MultipartFile file , @RequestParam UUID titleId) throws IOException {
        CareerPackageTemplateRequestDTO requestDTO = new CareerPackageTemplateRequestDTO(file, titleId);
        return ResponseEntity.ok(careerPackageTemplateService.createCareerPackage(requestDTO));
    }

    @PutMapping("/{packageId}")
    public ResponseEntity<CareerPackageTemplateResponseDTO> updateCareerPackage(@PathVariable UUID packageId, @RequestBody CareerPackageTemplateRequestDTO requestDTO) throws IOException {
        return ResponseEntity.ok(careerPackageTemplateService.updateCareerPackage(packageId,requestDTO));
    }

    @DeleteMapping("/{packageId}")
    public ResponseEntity<String> deleteCareerPackage(@PathVariable UUID packageId) {
        return ResponseEntity.ok(careerPackageTemplateService.deleteCareerPackage(packageId));
    }
}
