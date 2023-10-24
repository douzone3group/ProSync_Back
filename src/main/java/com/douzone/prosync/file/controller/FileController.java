package com.douzone.prosync.file.controller;

import com.douzone.prosync.common.SingleResponseDto;
import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.file.entity.FileInfo;
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

import java.util.List;


@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@Validated
@Tag(name = "file", description = "파일 API")
public class FileController {

    private final FileService fileService;

    @PostMapping
    @Operation(summary = "파일 등록", description = "파일을 업로드합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "file not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<SingleResponseDto<List<FileResponseDto>>> postFile(@Parameter(description = "MultipartFile", required = true) @RequestParam List<MultipartFile> files) {
        List<FileResponseDto> response = fileService.uploadFile(files);
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/{file-id}")
    @Operation(summary = "파일 삭제", description = "파일을 삭제합니다.", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "file not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity deleteFile(@Parameter(description = "파일식별자", required = true, example = "1") @PathVariable("file-id") Long fileId) {
        fileService.delete(fileId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "파일 조회", description = "테이블이름과 기본키를 통해 파일을 조회합니다..", tags = "file")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved"),
            @ApiResponse(code = 404, message = "file not found"),
            @ApiResponse(code = 500, message = "server error"),
    })
    @GetMapping
    public ResponseEntity<SingleResponseDto<List<FileResponseDto>>> findFileList(@RequestParam("tableKey") Long tableKey,
                                                                                 @RequestParam("tableName") String tableName) {
        List<FileResponseDto> response = fileService.findFilesByTableInfo(FileRequestDto.create(FileInfo.FileTableName.valueOf(tableName), tableKey), true);
        return new ResponseEntity(new SingleResponseDto<>(response), HttpStatus.OK);
    }

}
