package com.douzone.prosync.file.service;

import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {


    List<FileResponseDto> uploadFile(List<MultipartFile> multipartFiles);

    // soft delete
    void delete(Long fileId);

    void deleteActualFile(Long fileId);

    void deleteActualFileList(List<Long> fileIdList);

    List<FileResponseDto> findFileList(List<Long> fileIds, Boolean isResponse);

    File findFile(Long fileId);

    void saveFileInfo(FileInfo fileInfo);

    void saveFileInfoList(List<FileInfo> fileInfoList);

    List<FileResponseDto> findFilesByTableInfo(String tableName, Long tableKey);

}
