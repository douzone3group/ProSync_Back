package com.douzone.prosync.project.dto.response;

import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@ApiModel("GetProjectsResponse")
public class GetProjectsResponse {

    @ApiModelProperty(example = "프로젝트 아이디")
    private Long projectId;

    @ApiModelProperty(example = "프로젝트 이름")
    private String title;

    @ApiModelProperty(example = "프로젝트 시작 날짜")
    private String startDate;

    @ApiModelProperty(example = "프로젝트 종료 날짜")
    private String endDate;

    private String createdAt;

    private Long memberId;
    private String name;
    private String profileImage;

    private List<MemberProjectResponseDto> projectMembers;

    public void setProjectMembers(List<MemberProjectResponseDto> projectMembers) {
        this.projectMembers = projectMembers;
    }
}