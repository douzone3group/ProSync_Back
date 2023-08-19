package com.douzone.prosync.file.controller;

import com.douzone.prosync.file.FileDto;
import com.douzone.prosync.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/members/profile")
    public ResponseEntity postUserProfile(@RequestParam("image") MultipartFile image,
                                          Principal principal) throws IOException {
        fileService.uploadUserProfileImage(image, Long.parseLong(principal.getName()));
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/tasks/{task-id}/file")
    public ResponseEntity postTaskFile(@PathVariable("task-id") Long taskId,
                                       @RequestParam("files") List<MultipartFile> files,
                                       Principal principal) throws IOException {
        List<FileDto> response = fileService.uploadTaskFile(files, taskId);
        //TODO : DATA에 감싸서 리턴하기
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/files/{file-id}")
    public ResponseEntity deleteTaskFile(@PathVariable("file-id") Integer fileId,
                                         Principal principal) {

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
