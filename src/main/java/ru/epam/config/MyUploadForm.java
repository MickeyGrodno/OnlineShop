package ru.epam.config;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MyUploadForm {
    private String description;
    private Long id;
    private MultipartFile[] filesData;
}
