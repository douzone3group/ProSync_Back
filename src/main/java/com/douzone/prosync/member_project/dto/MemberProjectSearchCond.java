package com.douzone.prosync.member_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberProjectSearchCond {
    private Long projectId;

    private String search;
}
