package com.douzone.prosync.project.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;




@Getter
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String title;
    private String intro;

    private LocalDateTime createdAt;
    private String startDate;
    private String endDate;
    private LocalDateTime modifiedAt;

    private Boolean isPublic;
    private String projectImage;
    private Boolean isDeleted;
}
