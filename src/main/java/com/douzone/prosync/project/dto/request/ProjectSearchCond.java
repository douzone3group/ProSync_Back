package com.douzone.prosync.project.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectSearchCond {

    // title
    // project_member -> admin
    private String search;

    // project - bookmark - member
    private Boolean bookmark;

    //sort - endDate, latest(기본)
    private String sort;

    private Long memberId;
}
