package com.douzone.prosync.file.service;

import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {


    List<FileResponseDto> uploadFile(List<MultipartFile> multipartFiles);

    void delete(Long fileInfoId);

    List<FileResponseDto> findFilesByTableInfo(FileRequestDto dto, Boolean isResponse);

    //  TODO: 각 도메인 서비스 계층에서 쓰기
    void saveFileInfo(FileInfo fileInfo);

    void saveFileInfoList(List<FileInfo> fileInfoList);

    void deleteFileList(FileRequestDto dto);

    File findFile(Long fileId);

}

