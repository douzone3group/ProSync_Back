package com.douzone.prosync.task.dto;

import com.douzone.prosync.status.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

public class TaskResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GetTaskResponse {
        private Integer taskId;
        private String classification;
        private String title;
        private String detail;
        private String startDate;
        private String endDate;
        private TaskStatus taskStatus;

        private Timestamp createdAt;
        private Timestamp modifiedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleResponse {

        private Integer taskId;
        private String classification;
        private String title;
        private Timestamp createdAt;
    }
}
