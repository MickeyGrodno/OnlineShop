package ru.epam.dto;

import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class UploadResultDto {
    private String description;
    private List<File> uploadedFiles;
}
