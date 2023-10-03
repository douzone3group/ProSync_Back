package com.douzone.prosync.authorization.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProjectAuthorizationResponse {

    private Long memberId;
    private Long projectId;
    private Long authorityId;
    private String authority;
    private String status;

}

