package com.douzone.prosync.task.dto.request;

import com.douzone.prosync.validator.HtmlTagExcluded;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("[REQUEST] TASK - PATCH")
public class TaskPatchDto {

    @ApiModelProperty(hidden = true)
    private Long taskId;

    @ApiModelProperty(value = "분류", example = "분류")
    @Length(min = 1)
    private String classification;

    @ApiModelProperty(value = "제목", example = "제목")
    @Length(min = 1, max = 200, message = "제목은 200자 이내여야 합니다")
    private String title;

    @ApiModelProperty(value = "상세설명", example = "상세설명")
    @HtmlTagExcluded(message = "업무 상세는 1 ~ 1000자 이내여야 합니다.")
    private String detail;

    @ApiModelProperty(value = "시작일자", example = "2023-10-01")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
    private String startDate;

    @ApiModelProperty(value = "종료일자", example = "2023-10-31")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
    private String endDate;

    @ApiModelProperty(value = "업무상태식별자", example = "1")
    private Long taskStatusId;

    @ApiModelProperty(value = "파일식별자", example = "[1, 2, 3]")
    private List<Long> fileIds = new ArrayList<>();
}
