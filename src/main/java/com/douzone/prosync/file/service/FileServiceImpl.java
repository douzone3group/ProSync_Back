package com.douzone.prosync.file.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.component.FileHandler;
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

    // 파일 삭제 (soft)
    @Override
    public void delete(Long fileId) {
        Integer row = fileMapper.delete(fileId);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    // 파일 삭제
    @Override
    public void deleteActualFile(Long fileId) {
        File byId = fileMapper.findById(fileId);
        Integer row = fileMapper.delete(fileId);
        if (row < 1) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
        }
        fileHandler.delete(byId.getFileName());
    }

    // 파일 목록 삭제
    @Override
    public void deleteActualFileList(List<Long> fileIdList) {
        List<File> byIdList = fileMapper.findByIdList(fileIdList);
        fileHandler.deleteAll(
                byIdList
                        .stream()
                        .map(File::getFileName)
                        .collect(Collectors.toList())
        );
        fileMapper.deleteAll(fileIdList);
    }

    // 파일 조회
    @Override
    public File findFile(Long fileId) {
        return fileMapper.findById(fileId);
    }

    // fileId로 파일 목록 조회
    @Override
    public List<FileResponseDto> findFileList(List<Long> fileIds, Boolean isResponse) {
        List<File> files = fileMapper.findByIdList(fileIds);
        return files.stream().map(file -> FileResponseDto.response(file, isResponse)).collect(Collectors.toList());
    }

    @Override
    public List<FileResponseDto> findFilesByTableInfo(String tableName, Long tableKey) {
        return fileMapper.findFilesByTableInfo(tableName, tableKey);
    }

    @Override
    public void saveFileInfo(FileInfo fileInfo) {
        fileMapper.saveFileInfo(fileInfo);
    }

    @Override
    public void saveFileInfoList(List<FileInfo> fileInfoList) {
        fileMapper.saveFileInfoList(fileInfoList);
    }
}