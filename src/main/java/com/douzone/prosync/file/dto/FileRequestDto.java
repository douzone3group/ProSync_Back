package com.douzone.prosync.file.dto;

import com.douzone.prosync.file.entity.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileRequestDto {

    private FileInfo.FileTableName tableName;

    private Long tableKey;

    public static FileRequestDto create(FileInfo.FileTableName tableName, Long tableKey) {
        return FileRequestDto.builder()
                .tableName(tableName)
                .tableKey(tableKey)
                .build();
    }
}
