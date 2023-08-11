package com.douzone.prosync.task.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

//TODO: getter, setter 제거
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;
    private String classification;
    private String title;
    private String detail;
    private String startDate;
    private String endDate;

    //TODO : 업무 상태 로직 변경
    private String taskStatus;

    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private Boolean isDeleted;
    private Integer projectId;
}
