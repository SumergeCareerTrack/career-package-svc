package com.sumerge.careertrack.career_package_svc.controllers;

import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import com.sumerge.careertrack.career_package_svc.services.EmployeeCareerPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/employee-packages")
@RequiredArgsConstructor
public class EmployeeCareerPackageController {

    private final EmployeeCareerPackageService employeeCareerPackageService;

    @GetMapping
    public ResponseEntity<List<EmployeeCareerPackageResponseDTO>> getAllEmployeeCareerPackages() {
        return ResponseEntity.ok(employeeCareerPackageService.getAllEmployeeCareerPackages());
    }

    @GetMapping("/{employeePackageId}")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> getEmployeeCareerPackageById(@PathVariable UUID employeePackageId) {
        return ResponseEntity.ok(employeeCareerPackageService.getEmployeeCareerPackageByPackageId(employeePackageId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeCareerPackageResponseDTO>> getEmployeeCareerPackageByEmployeeId(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(employeeCareerPackageService.getEmployeeCareerPackageByEmployeeId(employeeId));
    }

    @PostMapping
    public ResponseEntity<EmployeeCareerPackageResponseDTO> createEmployeeCareerPackage(@RequestParam MultipartFile file ,
                                                                                        @RequestParam UUID employeeId) throws Exception {
        EmployeeCareerPackageRequestDTO requestDTO = new EmployeeCareerPackageRequestDTO(employeeId,file);
        return ResponseEntity.ok(employeeCareerPackageService.createEmployeeCareerPackage(requestDTO));
    }

    @PutMapping("/{employeePackageId}")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> updateEmployeeCareerPackage(@PathVariable UUID employeePackageId,
                                                                                        @RequestParam MultipartFile file) throws Exception{
        EmployeeCareerPackageRequestDTO requestDTO = new EmployeeCareerPackageRequestDTO(employeePackageId,file);
        return ResponseEntity.ok(employeeCareerPackageService.updateEmployeeCareerPackage(employeePackageId , requestDTO));
    }

    @DeleteMapping("/{employeePackageId}")
    public ResponseEntity<String> deleteEmployeeCareerPackage(@PathVariable UUID employeePackageId) {
        return ResponseEntity.ok(employeeCareerPackageService.deleteEmployeeCareerPackage(employeePackageId));
    }

    @PutMapping("/{employeePackageId}/approve")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> approveEmployeeCareerPackage(@PathVariable UUID employeePackageId , @RequestBody String comment) {
        return ResponseEntity.ok(employeeCareerPackageService.approveEmployeeCareerPackage(employeePackageId , comment));
    }

    @PutMapping("/{employeePackageId}/reject")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> rejectEmployeeCareerPackage(@PathVariable UUID employeePackageId , @RequestBody String comment) {
        return ResponseEntity.ok(employeeCareerPackageService.rejectEmployeeCareerPackage(employeePackageId , comment));
    }
}
