package com.douzone.prosync.file.entity;

import lombok.*;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class FileInfo {

    private Long fileInfoId;

    private FileTableName tableName;

    private Long tableKey;

    private Long fileId;

    private LocalDateTime deletedAt;

    public enum FileTableName {
        MEMBER,
        TASK,
        PROJECT,
        COMMENT
    }

    public static FileInfo createFileInfo(FileTableName tableName, Long tableKey, Long fileId) {
        return FileInfo.builder()
                .tableName(tableName)
                .tableKey(tableKey)
                .fileId(fileId)
                .build();
    }

    public static List<FileInfo> createFileInfos(List<Long> fileIds, FileTableName tableName, Long tableKey) {
        return fileIds
                .stream()
                .map(fileId -> FileInfo.createFileInfo(tableName, tableKey, fileId))
                .collect(Collectors.toList());
    }
}
