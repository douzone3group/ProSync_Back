package com.douzone.prosync.task_status.dto;

import com.douzone.prosync.task_status.basic.BasicTaskStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class TaskStatusDto {

    @ApiModel("[REQUEST] TASK_STATUS - POST")
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDto {

        @ApiModelProperty(hidden = true)
        private Long taskStatusId;

        @ApiModelProperty(value = "업무상태", required = true, example = "TODO")
        @NotBlank
        private String taskStatus;

        @ApiModelProperty(value = "색상", required = true, example = "#000000")
        @NotBlank
        @Pattern(regexp = "^#\\w{6}$", message = "#6자리를 입력하세요. (예 : #000000)")
        private String color;

        @Setter
        @NotNull
        @ApiModelProperty(value = "순서", required = true, example = "1")
        private Integer seq;

        public static PostDto createNoStatus() {
            return PostDto.builder()
                    .taskStatus(BasicTaskStatus.NO_STATUS.getTaskStatus())
                    .color(BasicTaskStatus.NO_STATUS.getColor())
                    .seq(BasicTaskStatus.NO_STATUS.getSeq())
                    .build();
        }

        public static PostDto createTodo() {
            return PostDto.builder()
                    .taskStatus(BasicTaskStatus.TODO.getTaskStatus())
                    .color(BasicTaskStatus.TODO.getColor())
                    .seq(BasicTaskStatus.TODO.getSeq())
                    .build();
        }

        public static PostDto createInProgress() {
            return PostDto.builder()
                    .taskStatus(BasicTaskStatus.IN_PROGRESS.getTaskStatus())
                    .color(BasicTaskStatus.IN_PROGRESS.getColor())
                    .seq(BasicTaskStatus.IN_PROGRESS.getSeq())
                    .build();
        }

        public static PostDto createDone() {
            return PostDto.builder()
                    .taskStatus(BasicTaskStatus.DONE.getTaskStatus())
                    .color(BasicTaskStatus.DONE.getColor())
                    .seq(BasicTaskStatus.DONE.getSeq())
                    .build();
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
        @Pattern(regexp = "^#\\w{6}$", message = "#6자리로 입력하세요. (예 : #000000)")
        private String color;

        @ApiModelProperty(value = "보여질 순서 (순서지정하지 않을 경우 -> 0)", example = "1")
        private Integer seq;

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
