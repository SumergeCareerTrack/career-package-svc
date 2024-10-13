package com.sumerge.careertrack.career_package_svc.entities.responses;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CareerPackageTemplateResponseDTO {

    @NonNull
    private UUID id;

    @NonNull
    private String url;

    @NonNull
    private UUID titleId;
}
