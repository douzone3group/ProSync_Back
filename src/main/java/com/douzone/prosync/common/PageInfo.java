package com.douzone.prosync.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel("[RESPONSE] LIST - GET (PAGEINFO)")
public class PageInfo {

    @ApiModelProperty(value = "현재 페이지", example = "1")
    private int page;

    @ApiModelProperty(value = "페이지당 요소 개수", example = "10")
    private int size;

    @ApiModelProperty(value = "총 요소 개수", example = "10")
    private long totalElements;

    @ApiModelProperty(value = "총 페이지 수", example = "10")
    private int totalPages;

}
