package com.douzone.prosync.member_project.repository;

import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.dto.MemberProjectSearchCond;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberProjectMapper {

    @Insert("insert into member_project (member_id, project_id, status) values (#{memberId}, #{projectId}, #{status})")
    Integer saveProjectMember(@Param("memberId") Long memberId, @Param("projectId") Long projectId, @Param("status") MemberProject.MemberProjectStatus status);

    @Update("update member_project set status = #{status} where member_project_id = #{projectMemberId}")
    Integer deleteProjectMember(@Param("projectMemberId") Long projectMemberId, @Param("status") MemberProject.MemberProjectStatus status);

    Integer updateAuthorityOfProjectMember(@Param("projectMemberId") Long projectMemberId, @Param("authority") ProjectMemberAuthority authority);

    Optional<MemberProjectResponseDto> findProjectMember(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    List<MemberProjectResponseDto> findProjectMembers(MemberProjectSearchCond searchCond);

    @Select("select * from member_project where member_project_id = #{projectMemberId}")
    Optional<MemberProject> findProjectMemberById(Long projectMemberId);

    @Update("update member_project set status = #{status} where project_id = #{projectId} and member_id = #{memberId}")
    void updateStatusOfProjectMember(@Param("projectId") Long projectId, @Param("memberId") Long memberId, @Param("status") MemberProject.MemberProjectStatus status);

    @Select("select project_id from member_project where member_project_id = #{projectMemberId}")
    Long findProjectByProjectMemberId(Long projectMemberId);

    @Insert("insert into member_project(project_id, member_id, authority_id, status) values(#{projectId}, #{memberId}, 3, #{status})")
    Integer saveProjectAdmin(@Param("projectId") Long projectId, @Param("memberId") Long memberId, @Param("status") MemberProject.MemberProjectStatus status);

    List<Long> findMemberIdsListById(@Param("memberProjectsId")List<Long> memberProjects);

    List<Long> findMemberIdsListByIdAll(@Param("memberProjectsId")List<Long> memberProjects);

    List<Long> findProjectIdsByMemberId(Long memberId);


    @Select("select mp.member_id from member_project mp join project_authority pa on mp.authority_id = pa.authority_id where project_id = #{projectId} and pa.authority = 'ADMIN'")
    Long findAdminByProjectId(Long projectId);



}
