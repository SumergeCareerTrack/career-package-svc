package com.sumerge.careertrack.career_package_svc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoadFile {

    private String fileName;
    private String fileType;
    private String fileSize;
    private byte[] file;

}