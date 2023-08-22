package com.douzone.prosync.file.controller;

import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.file.dto.FileDto;
import com.douzone.prosync.file.service.FileService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

//    @PostMapping("/members/profile")
//    @Operation(summary = "회원 프로필 변경", description = "회원 프로필을 설정합니다.", tags = "file")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "successfully retrieved"),
//            @ApiResponse(code = 500, message = "server error"),
//    })
//    public ResponseEntity postUserProfile(@Parameter(description = "이미지파일(형식 : jpeg/jpg/png/gif)", required = true) @RequestParam("image") MultipartFile image,
//                                          Principal principal) throws IOException {
//        fileService.uploadUserProfileImage(image, Long.parseLong(principal.getName()));
//        return new ResponseEntity(HttpStatus.OK);
//    }


    // 이 api는 필요가 없습니다.
    @PostMapping("/files/{target}/{target-id}")
    @Operation(summary = "파일 등록", description = "특정 업무나 댓글에 대한 파일을 업로드합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<List<FileDto>>> postTaskFile(@Parameter(description = "대상", required = true, example = "comments or tasks") @PathVariable("target") String target,
                                                                         @Parameter(description = "대상식별자", required = true, example = "1") @PathVariable("target-id") Long targetId,
                                                                         @Parameter(description = "업로드할 파일들", required = true) @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                                                         Principal principal) {
        List<FileDto> response = fileService.uploadFile(files, targetId, target, Long.parseLong(principal.getName()));
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }


    // 이 함수는 동작하지 않을 것입니다.
    @DeleteMapping("/files/{file-id}")
    @Operation(summary = "파일 삭제", description = "특정 업무나 댓글에 대한 파일을 삭제합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "task not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity deleteTaskFile(@Parameter(description = "파일식별자", required = true, example = "1") @PathVariable("file-id") Long fileId,
                                         Principal principal) {
        fileService.deleteFile(fileId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    // 이 api는 필요가 없습니다.
    @GetMapping("/files/{target}/{target-id}")
    @Operation(summary = "파일 목록 조회", description = "특정 업무나 댓글에 대한 파일 목록을 조회합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<List<FileDto>>> getFiles(@Parameter(description = "대상", required = true, example = "comments or tasks") @PathVariable("target") String target,
                                                                     @Parameter(description = "대상식별자", required = true, example = "1") @PathVariable("target-id") Long targetId,
                                                                     Principal principal) {
        return new ResponseEntity(new SingleResponseDto<>(fileService.findFiles(target, targetId, Long.parseLong(principal.getName()), true)), HttpStatus.OK);
    }

}
