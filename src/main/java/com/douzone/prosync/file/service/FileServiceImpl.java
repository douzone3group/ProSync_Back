package com.douzone.prosync.file.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.component.FileHandler;
import com.douzone.prosync.file.dto.FileDto;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.repository.FileMapper;
import com.douzone.prosync.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;

    private final TaskService taskService;

    private final FileHandler fileHandler;

    /**
     * 회원 프로필 이미지 저장/수정
     */
//    @Override
//    public String uploadUserProfileImage(MultipartFile multipartFile, Long memberId) {
//
//        String fileName = multipartFile.getOriginalFilename();
//        //파일 형식
//        String ext = fileName.split("\\.")[1];
//
//        // user profile image - jpg, jpeg, png, gif 허용
//        if (!List.of("jpg", "jpeg", "png", "gif").contains(ext)) {
//            throw new ApplicationException(ErrorCode.INVALID_FILE_TYPE);
//        }
//
//        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
//        fileName += "-" + date.format(new Date());
//
//        ObjectMetadata om = new ObjectMetadata();
//        om.setContentType(multipartFile.getContentType());
//
//        List<FileDto> files = findFiles("members", memberId, memberId, false);
//        deleteUserProfileImage(files);
//
//        try {
//            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), om).withCannedAcl(CannedAccessControlList.PublicRead));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String path = amazonS3Client.getUrl(bucket, fileName).toString();
//
//        // save file
//        com.douzone.prosync.file.entity.File createdFile = com.douzone.prosync.file.entity.File.create(multipartFile.getSize(), path, fileName);
//        fileMapper.save(createdFile);
//
//        // save file_info
//        FileInfo fileInfo = FileInfo.create("members", memberId, createdFile.getFileId());
//        fileMapper.saveFileInfo(fileInfo);
//
//        return path;
//    }


    // 기존 프로필 이미지가 시스템 기본이미지가 아닌 경우 해당 이미지를 S3에서 지움
//    private void deleteUserProfileImage(List<FileDto> files) {
//        if (files.size() != 0) {
//            FileDto file = files.get(0);
//            String fileName = file.getFileName();
//
//            amazonS3Client.deleteObject(bucket, fileName);
//
//            Integer deleteFileInfo = fileMapper.deleteFileInfo(file.getFileId());
//
//            if (deleteFileInfo != 1) {
//                throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
//            }
//
//            fileMapper.deleteFile(file.getFileId());
//        }
//    }


    /**
     * 업무 or 댓글 파일 저장
     */
    @Override
    public List<FileDto> uploadFile(List<MultipartFile> multipartFiles, Long targetId, String target, Long memberId) {

//        verifyTarget(target, targetId, memberId);
        List<File> fileList = fileHandler.saveAll(multipartFiles);
        fileMapper.saveFileList(fileList);
        // 이부분은 여기에 있으면 안됩니다.
        // 여기는 파일 파일 서비스를 호출한 곳에서 처리를 해야 합니다.
//        fileMapper.saveFileInfoList(
//                fileList.stream().map(file -> FileInfo.create(
//                        "",
//                        -1L,
//                        file.getFileId()
//                )).collect(Collectors.toList())
//        );

        return fileList
                .stream()
                .map(file -> FileDto.response(file, true))
                .collect(Collectors.toList());

//        return multipartFiles.stream().map((multipartFile) -> {
//            File createdFile = fileHandler.save(multipartFile);
//
//            // save file
//            fileMapper.save(createdFile);
//
//            // save file_info
//            FileInfo fileInfo = FileInfo.create(target, targetId, createdFile.getFileId());
//            fileMapper.saveFileInfo(fileInfo);
//
//            return (FileDto.response(createdFile, true));
//
//        }).collect(Collectors.toList());
    }

    /**
     * 업무 or 댓글 파일 삭제 (soft delete, s3 삭제되지 않음)
     */
    @Override
    public void delete(Long fileId) {
        fileMapper.delete(fileId);
    }

    public void deleteActualFile(Long fileId) {
        File byId = fileMapper.findById(fileId);
        fileHandler.delete(byId.getFileName());
        fileMapper.delete(fileId);
    }

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

    /**
     * 업무 or 댓글 파일 목록 조회
     */
    @Override
    public List<FileDto> findFiles(String target, Long targetId, Long memberId, Boolean isResponse) {
//        verifyTarget(target, targetId, memberId);
        List<File> files = fileMapper.findFiles(target, targetId);
        return files.stream().map(file -> FileDto.response(file, isResponse)).collect(Collectors.toList());
    }

    @Override
    public void deleteFile(Long fileId) {
        fileMapper.deleteFile(fileId);
    }


    // 존재하는 대상인지 확인
    // 이 확인은 로직이 파일을 넣는 곳에서 확인하는 것이 맞습니다.
    private void verifyTarget(String target, Long targetId, Long memberId) {
        if (target.equals("tasks")) {
            taskService.findTask(targetId, memberId);
        } else if (target.equals("comments")) {
            // TODO : comments 완성후 추가예
        } else if (!target.equals("members")) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE);
        }
    }

}
