package com.douzone.prosync.file.entity;

import lombok.*;

@Builder
@Getter
public class FileInfo {

    private Long fileInfoId;

    private String tableName;

    private Long tableKey;

    private Long fileId;

    public static FileInfo create(String tableName, Long tableKey, Long fileId) {
        return FileInfo.builder()
                .tableName(tableName)
                .tableKey(tableKey)
                .fileId(fileId)
                .build();
    }
}
