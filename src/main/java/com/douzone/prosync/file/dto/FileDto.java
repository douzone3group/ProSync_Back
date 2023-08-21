package com.douzone.prosync.file.dto;

import com.douzone.prosync.file.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileDto {
    private Long fileId;
    private Long size;
    private String path;
    private String fileName;

    public static FileDto response(File file, Boolean isResponse) {
        String fileName = isResponse ? changeFileNameFormat(file.getFileName()) : file.getFileName();
        return FileDto.builder()
                .fileId(file.getFileId())
                .path(file.getPath())
                .size(file.getSize())
                .fileName(fileName)
                .build();
    }

    private static String changeFileNameFormat(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("-"));
    }
}
