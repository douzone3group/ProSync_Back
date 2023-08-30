package com.douzone.prosync.task_status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class TaskStatusDto {

    @ApiModel("[REQUEST] TASK_STATUS - POST")
    @Getter
    public static class PostDto {

        @ApiModelProperty(hidden = true)
        private Long taskStatusId;

        @ApiModelProperty(value = "업무상태", required = true, example = "TODO")
        @NotBlank
        private String taskStatus;

        @ApiModelProperty(value = "색상", required = true, example = "#000000")
        @NotBlank
        @Pattern(regexp = "#\\d{6}", message = "#6자리 숫자 형식으로 입력하세요. (예 : #000000)")
        private String color;

        @ApiModelProperty(hidden = true)
        private LocalDateTime createdAt = LocalDateTime.now();

        public PostDto(String taskStatus, String color) {
            this.taskStatus = taskStatus;
            this.color = color;
        }
    }

    @Getter
    @ApiModel("[REQUEST] TASK_STATUS - PATCH")
    public static class PatchDto {

        @ApiModelProperty(hidden = true)
        private Long taskStatusId;

        @ApiModelProperty(value = "업무상태", example = "TODO")
        private String taskStatus;


        @ApiModelProperty(value = "색상", example = "#000000")
        @Pattern(regexp = "#\\d{6}", message = "#6자리 숫자 형식으로 입력하세요. (예 : #000000)")
        private String color;

        @ApiModelProperty(value = "보여질 순서 (순서지정하지 않을 경우 -> 0)", example = "1")
        private Integer seq;

        @ApiModelProperty(hidden = true)
        private LocalDateTime modifiedAt = LocalDateTime.now();

        public void setTaskStatusId(Long taskStatusId) {
            this.taskStatusId = taskStatusId;
        }
    }

    @Getter
    @ApiModel("[RESPONSE] TASK_STATUS - GET")
    public static class GetResponseDto {

        @ApiModelProperty(value = "업무상태식별자", example = "1")
        private Long taskStatusId;

        @ApiModelProperty(value = "색상", example = "#000000")
        private String color;

        @ApiModelProperty(value = "업무상태", example = "TODO")
        private String taskStatus;

        @ApiModelProperty(value = "보여질 순서 (순서지정 x 는 0으로 표시)", example = "1")
        private Integer seq;

        @ApiModelProperty(value = "프로젝트식별자", example = "1")
        private Long projectId;
    }


    @Getter
    @AllArgsConstructor
    @ApiModel("[RESPONSE] TASK_STATUS")
    public static class SimpleResponseDto {

        @ApiModelProperty(value = "업무상태식별자", example = "1")
        private Long taskStatusId;
    }
}
