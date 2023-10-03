package com.douzone.prosync.log.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLogDto {

    private Long memberLogId;

    private Long memberId;

    private Long logId;

}
