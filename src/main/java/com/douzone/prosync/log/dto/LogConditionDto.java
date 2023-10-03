package com.douzone.prosync.log.dto;

import com.douzone.prosync.log.logenum.LogCode;
import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LogConditionDto {

    // 로그의 원인 제공자
    private Long fromMemberId;

    // 로그 코드
    private LogCode code;

    // 단수의 대상
    private Long memberId;

    // 복수의 대상
    private List<Long> memberIds;

    // 프로젝트_멤버 id
    private Long projectMemberId;

    // 해당 프로젝트 id
    private Long projectId;

    // 해당 업무 id
    private Long taskId;

    // 회원 탈퇴 시 나가야하는 프로젝트 id들
    private List<Long> existProjectIds;

    // 넘겨주는 response 객체
    private Object subject;

    // 권한 변경 시 권한 객체
    private ProjectMemberAuthority authority;

}
