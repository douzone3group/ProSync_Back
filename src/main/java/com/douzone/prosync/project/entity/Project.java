package com.douzone.prosync.project.entity;

import lombok.*;

import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    private Integer projectId;
    private String name;
    private String intro;

    private Timestamp createdAt;
    private String startDate;
    private String endDate;
    private String modifiedAt;

    private Float progress;
    private Boolean publicyn;
    private String projectImage;
    private Boolean isDeleted;
}
