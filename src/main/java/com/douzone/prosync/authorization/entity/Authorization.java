package com.douzone.prosync.authorization.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Authorization {

    private Long authorityId;
    private String authority; // TODO : enum으로 교체
}
