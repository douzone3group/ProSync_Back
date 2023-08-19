package com.douzone.prosync.file;

import com.douzone.prosync.file.entity.File;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileDto {
    private Long fileId;
    private String path;

    public static FileDto response(File file) {
        return FileDto.builder()
                .fileId(file.getFileId())
                .path(file.getPath())
                .build();
    }
}
