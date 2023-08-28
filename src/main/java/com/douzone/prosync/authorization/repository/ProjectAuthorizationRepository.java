package com.douzone.prosync.authorization.repository;

import com.douzone.prosync.authorization.dto.GetProjectAuthorizationResponse;
import com.douzone.prosync.authorization.mapper.ProjectAuthorizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProjectAuthorizationRepository {

    private final ProjectAuthorizationMapper authorizationMapper;


    // 프로젝트 권한 가져오기
    public List<GetProjectAuthorizationResponse> getUserPermissonList(Long memberId){
        return authorizationMapper.getUserPermissonList(memberId);
    }

}
