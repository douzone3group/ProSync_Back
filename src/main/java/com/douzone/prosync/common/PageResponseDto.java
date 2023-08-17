package com.douzone.prosync.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageResponseDto<T> {

    @ApiModelProperty(value = "응답 데이터 리스트 예시")
    private List<T> data;
    private PageInfo pageInfo;

    public PageResponseDto(List<T> data, Page page) {
        this.data = data;
        pageInfo = new PageInfo(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}