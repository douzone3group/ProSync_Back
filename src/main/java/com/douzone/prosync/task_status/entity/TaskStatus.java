package com.douzone.prosync.task_status.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskStatusId;

    private String taskStatus;

    private Integer projectId;

    private String color;

    private Integer seq;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Boolean isDeleted;
}
