package com.sumerge.careertrack.career_package_svc.entities.responses;

import com.sumerge.careertrack.career_package_svc.entities.enums.ApprovalStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCareerPackageResponseDTO {

    @NonNull
    private UUID id;

    @NonNull
    private UUID employeeId;

    @NonNull
    private String fileId;

    @NonNull
    private String comment;

    @NonNull
    private ApprovalStatus approvalStatus;
}
