package com.douzone.prosync.task.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class TaskRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostDto {

        private Integer taskId;
        private String classification;

        @NotBlank
        private String title;
        private String detail;

        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
        private String startDate;

        @NotBlank
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
        private String endDate;
        private String taskStatus;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PatchDto {
        private Integer taskId;
        private String classification;
        private String title;
        private String detail;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
        private String startDate;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식을 확인하세요. (yyyy-mm-dd)")
        private String endDate;
        private String taskStatus;

    }
}
