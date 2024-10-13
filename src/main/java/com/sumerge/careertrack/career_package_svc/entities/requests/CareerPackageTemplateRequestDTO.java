package com.sumerge.careertrack.career_package_svc.entities.requests;


import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerPackageTemplateRequestDTO {

    @NonNull
    private String url;

    @NonNull
    private UUID titleId;
}
