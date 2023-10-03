package com.douzone.prosync.task.dto.response;

import com.douzone.prosync.task.dto.request.TaskMemberResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@ApiModel("GetTaskResponse")
public class GetTaskResponse {

        @ApiModelProperty(value = "업무식별자", example = "1")
        private Long taskId;

        @ApiModelProperty(value = "제목", example = "제목")
        private String title;

        @ApiModelProperty(value = "분류", example = "분류")
        private String classification;

        @ApiModelProperty(value = "상세설명")
        private String detail;

        @ApiModelProperty(value = "시작일자", example = "2023-10-01")
        private String startDate;

        @ApiModelProperty(value = "종료일자", example = "2023-10-31")
        private String endDate;

        @ApiModelProperty(value = "생성일자", example = "2023-10-01")
        private String createdAt;

        @ApiModelProperty(value = "최근수정일자", example = "2023-10-01")
        private String modifiedAt;

        @ApiModelProperty(value = "업무상태식별자", example = "1")
        private Long taskStatusId;

        @ApiModelProperty(value = "색상", example = "#000000")
        private String color;

        @ApiModelProperty(value = "업무상태", example = "TODO")
        private String taskStatus;

        @ApiModelProperty(value = "프로젝트식별자", example = "1")
        private Long projectId;

        @Getter
        @AllArgsConstructor
        public static class TaskMembersDto {

                private GetTaskResponse data;
                private List<TaskMemberResponseDto> taskMembers;
        }
}
