package com.sumerge.careertrack.career_package_svc.controllers;

import com.sumerge.careertrack.career_package_svc.entities.requests.EmployeeCareerPackageRequestDTO;
import com.sumerge.careertrack.career_package_svc.entities.responses.EmployeeCareerPackageResponseDTO;
import com.sumerge.careertrack.career_package_svc.services.EmployeeCareerPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<List<EmployeeCareerPackageResponseDTO>> getAllEmployeeCareerPackages(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null || size == 0) {
            // Fetch all employee career packages without pagination
            return ResponseEntity.ok(employeeCareerPackageService.getAllEmployeeCareerPackages());
        } else {
            // Paginated fetch
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(employeeCareerPackageService.getAllEmployeeCareerPackages(pageable));
        }
    }
    @GetMapping("/{employeePackageId}")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> getEmployeeCareerPackageById(@PathVariable UUID employeePackageId) {
        return ResponseEntity.ok(employeeCareerPackageService.getEmployeeCareerPackageByPackageId(employeePackageId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeCareerPackageResponseDTO>> getEmployeeCareerPackageByEmployeeId(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(employeeCareerPackageService.getEmployeeCareerPackageByEmployeeId(employeeId));
    }

    @PostMapping("/manager/{employeeId}")
    public ResponseEntity<List<EmployeeCareerPackageResponseDTO>> getAllSubordinateEmployeeCareerPackages(@PathVariable UUID employeeId , @RequestBody List<UUID> subordinateEmployeeIds) {
        return ResponseEntity.ok(employeeCareerPackageService.getAllSubordinateEmployeeCareerPackages(subordinateEmployeeIds));
    }

    @PostMapping("/{managerId}")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> createEmployeeCareerPackage(@RequestParam MultipartFile file ,
                                                                                        @RequestParam UUID employeeId,
                                                                                        @PathVariable String managerId) throws Exception {
        EmployeeCareerPackageRequestDTO requestDTO = new EmployeeCareerPackageRequestDTO(employeeId,file);
        return ResponseEntity.ok(employeeCareerPackageService.createEmployeeCareerPackage(requestDTO,managerId));
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

    @PutMapping("/{employeePackageId}/approve/{managerId}")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> approveEmployeeCareerPackage(@PathVariable UUID employeePackageId , @RequestBody String comment,@PathVariable String managerId) {
        return ResponseEntity.ok(employeeCareerPackageService.approveEmployeeCareerPackage(employeePackageId , comment,managerId));
    }

    @PutMapping("/{employeePackageId}/reject/{managerId}")
    public ResponseEntity<EmployeeCareerPackageResponseDTO> rejectEmployeeCareerPackage(@PathVariable UUID employeePackageId , @RequestBody String comment,@PathVariable String managerId) {
        return ResponseEntity.ok(employeeCareerPackageService.rejectEmployeeCareerPackage(employeePackageId , comment,managerId));
    }
}
