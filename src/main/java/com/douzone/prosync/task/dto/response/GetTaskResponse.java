package com.douzone.prosync.task.dto.response;

import com.douzone.prosync.task.entity.Task;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@ApiModel("[RESPONSE] TASK - GET")
public class GetTaskResponse {

        @ApiModelProperty(value = "업무식별자", example = "1")
        private Integer taskId;

        @ApiModelProperty(value = "분류", example = "분류")
        private String classification;

        @ApiModelProperty(value = "제목", example = "제목")
        private String title;

        @ApiModelProperty(value = "상세설명")
        private String detail;

        @ApiModelProperty(value = "시작일자", example = "2023-10-01")
        private String startDate;

        @ApiModelProperty(value = "종료일자", example = "2023-10-31")
        private String endDate;

        @ApiModelProperty(value = "업무상태", example = "TODO")
        private String taskStatus;

        @ApiModelProperty(value = "생성일자", example = "2023-10-01")
        private String createdAt;

        @ApiModelProperty(value = "최근수정일자", example = "2023-10-01")
        private String modifiedAt;

        public static GetTaskResponse of(Task task) {
            return GetTaskResponse.builder()
                    .taskId(task.getTaskId())
                    .classification(task.getClassification())
                    .title(task.getTitle())
                    .detail(task.getDetail())
                    .startDate(task.getStartDate())
                    .endDate(task.getEndDate())
                    .taskStatus(task.getTaskStatus())
                    .createdAt(task.getCreatedAt().toString())
                    .modifiedAt(task.getModifiedAt().toString()).build();

    }
}
