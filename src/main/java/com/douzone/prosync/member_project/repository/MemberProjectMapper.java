package com.douzone.prosync.member_project.repository;

import com.douzone.prosync.member_project.dto.MemberProjectRequestDto;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.entity.MemberProject;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberProjectMapper {

    @Insert("insert into member_project (member_id, project_id) values (#{memberId}, #{projectId})")
    Integer saveProjectMember(@Param("memberId") Long memberId, @Param("projectId") Long projectId);

    @Delete("delete from member_project where member_project_id = #{projectMemberId}")
    Integer deleteProjectMember(Long projectMemberId);

    Integer updateProjectMember(@Param("projectMemberId") Long projectMemberId, @Param("projectMember") MemberProjectRequestDto authority);

    Optional<MemberProjectResponseDto> findProjectMember(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    List<MemberProjectResponseDto> findProjectMembers(Long projectId);

    @Select("select * from member_project where member_project_id = #{projectMemberId}")
    Optional<MemberProject> findProjectMemberById(Long projectMemberId);

    @Delete("delete from member_project where project_id = #{projectId} and member_id = #{memberId}")
    void exitProjectMember(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Select("select project_id from member_project where member_project_id = #{projectMemberId}")
    Long findProjectByProjectMemberId(Long projectMemberId);

    @Insert("insert into member_project(project_id, member_id, authority_id) values(#{projectId}, #{memberId}, 3)")
    Integer saveProjectAdmin(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

}
