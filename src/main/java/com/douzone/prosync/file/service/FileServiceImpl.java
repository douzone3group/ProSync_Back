package com.douzone.prosync.file.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.component.FileHandler;
import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.repository.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    private final FileHandler fileHandler;


    // 파일 저장
    @Override
    public List<FileResponseDto> uploadFile(List<MultipartFile> multipartFiles) {

        List<File> fileList = fileHandler.saveAll(multipartFiles);
        fileMapper.saveFileList(fileList);

        return fileList
                .stream()
                .map(file -> FileResponseDto.response(file, true))
                .collect(Collectors.toList());
    }


    // 파일 1건 삭제 (파일 정보 soft delete)
    @Override
    public void delete(Long fileId) {
        FileInfo fileInfo = findFileInfoByFileId(fileId);
        Integer row = fileMapper.deleteFileInfo(fileInfo.getFileInfoId());
        if (row < 1) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
        }
    }


    // 파일 목록 삭제 (파일정보 soft delete)
    // 각 도메인 서비스 계층에서 도메인 삭제시 사용
    @Override
    public void deleteFileList(FileRequestDto dto) {
        fileMapper.deleteFileInfos(dto);
    }


    // 파일 목록 조회
    @Override
    public List<FileResponseDto> findFilesByTableInfo(FileRequestDto dto, Boolean isResponse) {
        List<FileResponseDto> files = fileMapper.findFilesByTableInfo(dto);
        if (isResponse) {
            files.forEach(file -> file.setFileName(FileResponseDto.getOriginalFileName(file.getFileName())));
        }
        return files;
    }


    // 파일 정보 저장
    // 각 도메인의 저장, 수정시 사용
    @Override
    public void saveFileInfo(FileInfo fileInfo) {
        findFile(fileInfo.getFileId());
        if (fileMapper.findFileInfoByFileId(fileInfo.getFileId()).isPresent()) {
            throw new ApplicationException(ErrorCode.FILE_INFO_EXISTS);
        }
        fileMapper.saveFileInfo(fileInfo);
    }


    // 파일 정보 리스트 저장
    // 각 도메인의 저장, 수정시 사용
    @Override
    public void saveFileInfoList(List<FileInfo> fileInfoList) {
        fileInfoList.forEach(fileInfo -> {
            // 존재하는 파일인지 검증
            findFile(fileInfo.getFileId());
            // 파일 정보가 존재하면 예외 처리
            if (fileMapper.findFileInfoByFileId(fileInfo.getFileId()).isPresent()) {
                throw new ApplicationException(ErrorCode.FILE_INFO_EXISTS);
            }
        });
        fileMapper.saveFileInfoList(fileInfoList);
    }


    // 파일 조회
    @Override
    public File findFile(Long fileId) {
        return fileMapper.findById(fileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
    }


    @Override
    public void checkFileExtForProfile(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".")+1, fileName.lastIndexOf("_"));
        List<String> imageExtList = List.of("jpg", "jpeg", "gif", "png");

        if (!imageExtList.contains(ext)) {
            throw new ApplicationException(ErrorCode.INVALID_FILE_TYPE);
        }
    }


    private FileInfo findFileInfoByFileId(Long fileId) {
        return fileMapper.findFileInfo(fileId).orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
    }



}