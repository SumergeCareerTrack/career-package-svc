package com.sumerge.careertrack.career_package_svc.entities.requests;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCareerPackageRequestDTO {

    @NonNull
    private UUID employeeId;

    @NonNull
    private MultipartFile file;
}
