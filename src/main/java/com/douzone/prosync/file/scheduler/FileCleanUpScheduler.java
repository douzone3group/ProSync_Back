package com.douzone.prosync.file.scheduler;

import com.douzone.prosync.file.component.FileHandler;
import com.douzone.prosync.file.entity.File;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.repository.FileMapper;
import com.douzone.prosync.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileCleanUpScheduler {

    private final FileService fileService;
    private final FileMapper fileMapper;
    private final FileHandler fileHandler;

    // 매일 자정 사용자가 삭제후 30일 경과된 파일을 삭제 처리
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredFiles() {

        LocalDateTime dateTime = LocalDateTime.now().minusDays(30);
        List<FileInfo> deletedFile = fileMapper.findDeletedFile(dateTime);
        for (FileInfo fileInfo : deletedFile) {

            // 테이블 삭제
            fileMapper.deleteActualFileInfo(fileInfo.getFileInfoId());
            fileMapper.deleteActualFile(fileInfo.getFileId());

            // 저장소 삭제
            File findFile = fileService.findFile(fileInfo.getFileId());
            fileHandler.delete(findFile.getFileName());
        }
    }

    // 3시간마다 파일정보에 없는 파일을 삭제
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void cleanupUnmatchedFile() {

        List<File> files = fileMapper.findFilesWithNoFileInfo();
        for (File file : files) {

            fileMapper.deleteActualFile(file.getFileId());
            fileHandler.delete(file.getFileName());

        }
    }

}
