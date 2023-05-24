package com.github.miho73.legacy.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadForm {
    private String name;
    private String explain;
    private int tags;
    private MultipartFile file;
    private String file_name;
}
