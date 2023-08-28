package com.douzone.prosync.authorization.mapper;

import com.douzone.prosync.authorization.dto.GetProjectAuthorizationResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectAuthorizationMapper {
    // 프로젝트 권한 가져오기
    List<GetProjectAuthorizationResponse> getUserPermissonList(Long memberId);
}
