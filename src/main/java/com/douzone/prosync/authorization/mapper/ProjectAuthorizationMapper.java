package com.douzone.prosync.authorization.mapper;

import com.douzone.prosync.authorization.dto.GetProjectAuthorizationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectAuthorizationMapper {
    // 프로젝트 권한 가져오기
    Optional<GetProjectAuthorizationResponse> getUserAuthorization(@Param("memberId") Long memberId, @Param("projectId") Long projectId);


}