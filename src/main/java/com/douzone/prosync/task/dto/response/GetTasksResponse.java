package com.douzone.prosync.task.dto.response;

import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task_status.entity.TaskStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ApiModel("[RESPONSE] TASK LIST - GET")
public class GetTasksResponse {

    @ApiModelProperty(value = "업무식별자", example = "1")
    private Integer taskId;

    @ApiModelProperty(value = "분류", example = "분류")
    private String classification;

    @ApiModelProperty(value = "제목", example = "제목")
    private String title;

    @ApiModelProperty(value = "시작일자", example = "2023-10-01")
    private String startDate;

    @ApiModelProperty(value = "종료일자", example = "2023-10-31")
    private String endDate;

    @ApiModelProperty(value = "생성일자", example = "2023-10-01")
    private String createdAt;

    @ApiModelProperty(value = "최근수정일자", example = "2023-10-01")
    private String modifiedAt;

    @ApiModelProperty(value = "업무상태식별자", example = "1")
    private Integer taskStatusId;

    @ApiModelProperty(value = "색상", example = "#000000")
    private String color;

    @ApiModelProperty(value = "업무상태", example = "TODO")
    private String taskStatus;

    @ApiModelProperty(value = "보여질 순서 (순서지정 x 는 0으로 표시)", example = "1")
    private Integer seq;

    public static GetTasksResponse of(Task task) {
        TaskStatus taskStatus = task.getTaskStatus();
        return GetTasksResponse.builder()
                .taskId(task.getTaskId())
                .classification(task.getClassification())
                .title(task.getTitle())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .createdAt(task.getCreatedAt().toString())
                .modifiedAt(task.getModifiedAt().toString())
                .taskStatusId(taskStatus.getTaskStatusId())
                .taskStatus(taskStatus.getTaskStatus())
                .seq(taskStatus.getSeq())
                .color(taskStatus.getColor()).build();
    }

    @Builder
    @Getter
    public static class PerTasksResponse {

        @ApiModelProperty(value = "업무상태식별자", example = "1")
        private Integer taskStatusId;

        @ApiModelProperty(value = "업무상태", example = "TODO")
        private String taskStatus;

        @ApiModelProperty(value = "색상", example = "#000000")
        private String color;

        @ApiModelProperty(value = "보여질 순서 (순서지정 x 는 0으로 표시)", example = "1")
        private Integer seq;

        private List<GetTasksResponse> list;

        public static List<PerTasksResponse> of(List<GetTasksResponse> list) {
            List<Integer> status = new ArrayList<>();
            List<PerTasksResponse> perTasksResponses = new ArrayList<>();
            list.forEach(res -> {
                if (!status.contains(res.getTaskStatusId())) {
                    status.add(res.getTaskStatusId());

                    // get list -> PerTaskResponse
                    List<GetTasksResponse> getTaskResponses = list.stream()
                            .filter(one -> one.getTaskStatusId() == res.getTaskStatusId())
                            .collect(Collectors.toList());

                    perTasksResponses.add(PerTasksResponse.builder()
                            .taskStatusId(res.getTaskStatusId())
                            .taskStatus(res.getTaskStatus())
                            .color(res.getColor())
                            .seq(res.getSeq())
                            .list(getTaskResponses)
                            .build());
                }
            });

            return perTasksResponses;
        }

    }

}