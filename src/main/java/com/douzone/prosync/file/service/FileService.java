package com.douzone.prosync.file.service;

import com.douzone.prosync.file.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

//    String uploadUserProfileImage(MultipartFile multipartFile, Long memberId);

    List<FileDto> uploadFile(List<MultipartFile> multipartFiles, Long targetId, String target, Long memberId);

    // soft delete
    void delete(Long fileId);

    List<FileDto> findFiles(String target, Long targetId, Long memberId, Boolean isResponse);

    void deleteFile(Long fileId);
}
