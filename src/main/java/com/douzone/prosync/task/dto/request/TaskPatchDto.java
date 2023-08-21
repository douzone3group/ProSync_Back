package com.douzone.prosync.task.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

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
    @Length(max = 500, message = "프로젝트 상세는 500자 이내여야 합니다")
    private String detail;

    @ApiModelProperty(value = "시작일자", example = "2023-10-01")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
    private String startDate;

    @ApiModelProperty(value = "종료일자", example = "2023-10-31")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
    private String endDate;

    @ApiModelProperty(value = "업무상태식별자", example = "1")
    private Integer taskStatusId;

    @ApiModelProperty(hidden = true)
    private LocalDateTime modifiedAt;
}
