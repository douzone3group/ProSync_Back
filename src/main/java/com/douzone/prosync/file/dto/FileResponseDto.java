package com.douzone.prosync.file.dto;

import com.douzone.prosync.file.entity.File;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileResponseDto {

    @ApiModelProperty(value = "파일식별자", example = "1")
    private Long fileId;

    @ApiModelProperty(value = "파일크기", example = "10000")
    private Long size;

    @ApiModelProperty(value = "파일경로", example = "http://~")
    private String path;

    @ApiModelProperty(value = "파일이름", example = "파일이름.jpg")
    private String fileName;

    @ApiModelProperty(value = "생성일자", example = "2023-08-27 22:25:26")
    private String createdAt;

    public static FileResponseDto response(File file, Boolean isResponse) {
        String fileName = isResponse ? changeFileNameFormat(file.getFileName()) : file.getFileName();
        return FileResponseDto.builder()
                .fileId(file.getFileId())
                .path(file.getPath())
                .size(file.getSize())
                .fileName(fileName)
                .createdAt(file.getCreatedAt().toString().replace("T", " "))
                .build();
    }

    private static String changeFileNameFormat(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("-"));
    }
}
