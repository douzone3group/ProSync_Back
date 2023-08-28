package com.douzone.prosync.project.entity;

import com.douzone.prosync.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;
    private String title;
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
