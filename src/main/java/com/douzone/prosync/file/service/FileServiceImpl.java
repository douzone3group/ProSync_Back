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

/**
 * 파일 삭제시 [파일_정보] 삭제와 [파일]의 soft delete
 * [파일_정보]와 [파일] 간 매칭되지 않은 경우 (=파일에만 데이터가 있을 경우) -> [파일] + [저장소] 삭제 처리
 * 파일 삭제한지 30일이 지나면 [저장소] 및 [파일] 삭제 처리
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    private final FileHandler fileHandler;


    // 파일 저장
    // 파일 식별자 리턴 - OK
    @Override
    public List<FileResponseDto> uploadFile(List<MultipartFile> multipartFiles) {

        List<File> fileList = fileHandler.saveAll(multipartFiles);
        fileMapper.saveFileList(fileList);

        return fileList
                .stream()
                .map(file -> FileResponseDto.response(file, true))
                .collect(Collectors.toList());
    }


    // 파일 1건 삭제 (soft delete)
    // 파일 정보 삭제 - OK
    @Override
    public void delete(Long fileInfoId) {
        FileInfo fileInfo = findFileInfo(fileInfoId);
        Integer row = fileMapper.deleteFileInfo(fileInfo.getFileInfoId());
        if (row < 1) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
        }
        fileMapper.deleteFile(fileInfo.getFileId()); // soft delete
    }


    // 파일 목록 삭제 (파일정보 삭제 + 파일 soft delete)
    // 각 도메인 서비스계층에서 삭제시 사용
    @Override
    public void deleteFileList(FileRequestDto dto) {
        fileMapper.deleteFiles(dto); // file - soft delete
        fileMapper.deleteFileInfos(dto);
    }


    // 파일 목록 조회
    @Override
    public List<FileResponseDto> findFilesByTableInfo(FileRequestDto dto, Boolean isResponse) {
        List<FileResponseDto> files = fileMapper.findFilesByTableInfo(dto);
        if (isResponse) {
            files.forEach(FileResponseDto::changeFileNameFormat);
        }
        return files;
    }


    // 파일 정보 저장
    // 각 도메인의 저장, 수정시 사용
    @Override
    public void saveFileInfo(FileInfo fileInfo) {
        fileMapper.saveFileInfo(fileInfo);
    }


    // 파일 정보 저장
    // 각 도메인의 저장, 수정시 사용
    @Override
    public void saveFileInfoList(List<FileInfo> fileInfoList) {
        fileMapper.saveFileInfoList(fileInfoList);
    }


    // 파일 조회
    // 파일 정보 식별자로 파일 정보를 찾음
    public File findFile(Long fileId) {
        return fileMapper.findById(fileId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
    }


    private FileInfo findFileInfo(Long fileInfoId) {
        return fileMapper.findFileInfo(fileInfoId).orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
    }


    // 저장소 파일 삭제
    private void deleteActualFile(Long fileId) {
        File file = findFile(fileId);
        fileHandler.delete(file.getFileName());
    }


    // 저장소 파일 목록 삭제
    // 파일 정보에 없는 파일인 경우 삭제 처리하는 로직에 사용
    private void deleteActualFileList(FileRequestDto dto) {
        List<FileResponseDto> files = findFilesByTableInfo(dto, false);
        fileHandler.deleteAll(
                files
                        .stream()
                        .map(FileResponseDto::getFileName)
                        .collect(Collectors.toList())
        );
    }
}