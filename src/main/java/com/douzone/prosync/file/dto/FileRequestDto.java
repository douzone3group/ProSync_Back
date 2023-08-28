package com.douzone.prosync.file.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class FileRequestDto {

    @ApiModelProperty(value = "파일아이디", required = true, example = "[1, 2, 3]")
    private List<Long> fileIds;
}
