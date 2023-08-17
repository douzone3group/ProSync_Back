package com.douzone.prosync.task.entity;

import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.task_status.entity.TaskStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    @Column(nullable = false)
    private String classification;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String detail;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column
    private Boolean isDeleted;

    @OneToOne
    @JoinColumn(name = "taskStatusId")
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

}
