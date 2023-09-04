package com.douzone.prosync.task.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@ApiModel("[REQUEST] TASK - POST")
public class TaskPostDto {

        @ApiModelProperty(hidden = true)
        private Long taskId;

        @ApiModelProperty(value = "분류", required = true, example = "분류")
        @NotNull
        private String classification;

        @ApiModelProperty(value = "제목", required = true, example = "제목")
        @NotBlank
        @Length(max = 200, message = "제목은 200자 이내여야 합니다")
        private String title;

        @ApiModelProperty(value = "상세설명", example = "상세설명")
        @NotNull
        @Length(max = 500, message = "프로젝트 상세는 500자 이내여야 합니다")
        private String detail;

        @ApiModelProperty(value = "시작일자", required = true, example = "2023-10-01")
        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
        private String startDate;

        @ApiModelProperty(value = "종료일자", required = true, example = "2023-10-31")
        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
        private String endDate;

        @ApiModelProperty(value = "업무상태", example = "1")
        private Long taskStatusId;

        @ApiModelProperty(value = "파일식별자", example = "[1, 2, 3]")
        private List<Long> fileIds = new ArrayList<>();


        public void setTaskStatusId(Long taskStatusId) {
                this.taskStatusId = taskStatusId;
        }
}
