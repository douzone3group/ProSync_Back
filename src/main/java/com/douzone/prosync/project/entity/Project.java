package com.douzone.prosync.project.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;




@Getter
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String startDate;

    @Column(nullable = false)
    private String endDate;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private Boolean isPublic;

    private String projectImage;

    @Column
    private Boolean isDeleted;

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }
}
