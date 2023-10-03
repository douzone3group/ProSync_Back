package com.douzone.prosync.authorization.repository;

import com.douzone.prosync.authorization.dto.GetProjectAuthorizationResponse;
import com.douzone.prosync.authorization.mapper.ProjectAuthorizationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectAuthorizationRepository {

    private final ProjectAuthorizationMapper authorizationMapper;


    // 프로젝트 권한 가져오기
    public Optional<GetProjectAuthorizationResponse> getUserAuthorization(Long memberId, Long projectId){
        return authorizationMapper.getUserAuthorization(memberId, projectId);
    }

}
