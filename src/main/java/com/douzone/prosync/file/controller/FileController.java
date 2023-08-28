package com.douzone.prosync.file.controller;

import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.file.FileDto;
import com.douzone.prosync.file.service.FileService;
import com.douzone.prosync.task.dto.response.TaskSimpleResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Tag(name = "file", description = "파일 API")
public class FileController {

    private final FileService fileService;

    @PostMapping("/members/profile")
    @Operation(summary = "회원 프로필 변경", description = "회원 프로필을 설정합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity postUserProfile(@RequestParam("image") MultipartFile image,
                                          Principal principal) throws IOException {
        fileService.uploadUserProfileImage(image, Long.parseLong(principal.getName()));
        return new ResponseEntity(HttpStatus.OK);
    }

    // TODO : 업무, 댓글 구분없이 이 메서드로 처리하는게 나은지 ? id랑 table 이름을 받아서.. ?
    @PostMapping("/tasks/{task-id}/file")
    @Operation(summary = "업무/댓글 파일 등록", description = "특정 업무나 댓글에 대한 파일을 업로드합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<List<FileDto>>> postTaskFile(@PathVariable("task-id") Long taskId,
                                                                         @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                                                         @RequestParam(value = "table", required = true) String table,
                                                                         Principal principal) {
        List<FileDto> response = fileService.uploadTaskFile(files, taskId);
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/files/{file-id}")
    @Operation(summary = "업무/댓글 파일 삭제.", description = "특정 업무나 댓글에 대한 파일을 삭제합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity deleteTaskFile(@PathVariable("file-id") Integer fileId,
                                         Principal principal) {

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
