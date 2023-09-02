package com.douzone.prosync.file.dto;

import com.douzone.prosync.file.entity.FileInfo;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class FileRequestDto {

    @NotBlank
    private FileInfo.FileTableName tableName;

    @NotBlank
    private Long tableKey;

    public static FileRequestDto create(FileInfo.FileTableName tableName, Long tableKey) {
        return FileRequestDto.builder()
                .tableName(tableName)
                .tableKey(tableKey)
                .build();
    }
}
