package com.douzone.prosync.project.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDateTime;




@Getter
@Entity
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;
    private String name;
    private String intro;

    private LocalDateTime createdAt;
    private String startDate;
    private String endDate;
    private LocalDateTime modifiedAt;

    private Float progress;
    private Boolean publicyn;
    private String projectImage;
    private Boolean isDeleted;
}
