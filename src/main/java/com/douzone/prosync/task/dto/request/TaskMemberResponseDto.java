package com.douzone.prosync.task.dto.request;

import com.douzone.prosync.member_project.entity.MemberProject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class TaskMemberResponseDto {

    @ApiModelProperty(value = "프로젝트회원식별자", example = "1")
    private Long memberProjectId;

    @ApiModelProperty(value = "프로젝트 회원 상태", example = "ACTIVE / QUIT")
    private MemberProject.MemberProjectStatus status;

    @ApiModelProperty(value = "회원식별자", example = "1")
    private Long memberId;

    @ApiModelProperty(value = "회원 프로필 이미지", example = "http://~")
    private String profileImage;

    @ApiModelProperty(value = "회원 이름", example = "홍길동")
    private String name;
}
