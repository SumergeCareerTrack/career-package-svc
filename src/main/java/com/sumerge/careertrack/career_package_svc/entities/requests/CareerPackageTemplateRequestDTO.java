package com.sumerge.careertrack.career_package_svc.entities.requests;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerPackageTemplateRequestDTO {

    @NonNull
    private MultipartFile file;

    @NonNull
    private UUID titleId;

    @NonNull
    private String name;

}
