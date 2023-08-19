package com.douzone.prosync.file.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class File {

    private Long fileId;

    private Long size;

    private String path;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

    private String fileName;

    public static File create(Long size, String path, String fileName) {
        return File.builder()
                .size(size)
                .path(path)
                .fileName(fileName)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
