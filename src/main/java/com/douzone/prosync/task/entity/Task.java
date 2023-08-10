package com.douzone.prosync.task.entity;

import com.douzone.prosync.status.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

//TODO:프로젝트와 연결
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

    private Integer taskId;
    private String classification;
    private String title;
    private String detail;
    private String startDate;
    private String endDate;

    //TODO : erd 추가 확인
    private TaskStatus taskStatus;

    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private Boolean isDeleted;

}
