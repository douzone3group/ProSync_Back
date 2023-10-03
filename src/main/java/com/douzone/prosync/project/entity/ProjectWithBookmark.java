package com.douzone.prosync.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWithBookmark {

    private Long projectId;
    private String title;

    private String projectImage;

    private Long bookmarkId;

    private String name;

    private String startDate;
    private String endDate;
}
