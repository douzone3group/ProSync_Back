package com.douzone.prosync.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


public class TaskRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostDto {

        private String classification;

        @NotBlank
        private String title;
        private String detail;

        @NotBlank
        private String startDate;

        @NotBlank
        private String endDate;
        private String taskStatus;


        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus.toUpperCase();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PatchDto {
        private Integer taskId;
        private String classification;
        private String title;
        private String detail;
        private String startDate;
        private String endDate;
        private String taskStatus;

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus.toUpperCase();
        }
    }
}
