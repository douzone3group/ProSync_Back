package com.douzone.prosync.authorization.filter;

import com.douzone.prosync.authorization.dto.GetProjectAuthorizationResponse;
import com.douzone.prosync.authorization.repository.ProjectAuthorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {


    private final ProjectAuthorizationRepository authorizationRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long memberId = getIdFromPrincipal(authentication);

        System.out.println(memberId); // 확인용

        String requestUri = request.getRequestURI();

        if(requestUri.startsWith("/api/v1/public/")){
            filterChain.doFilter(request, response);
        } else{
            List<GetProjectAuthorizationResponse> userPermission = getPermissionUrl(memberId);
            if(hasPermission(request, userPermission)){
                filterChain.doFilter(request, response);
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }


    /**
     *  principal에서 memberid 가져오기
     */
    Long getIdFromPrincipal(Authentication authentication){
        Object principal = authentication.getPrincipal();
        Long memberId = (Long)principal;
        return memberId;
    }

    /**
     *  MemberId의 n번 프로젝트 권한
     */
    List<GetProjectAuthorizationResponse> getPermissionUrl(Long memberId){
        return authorizationRepository.getUserPermissonList(memberId);
    }

    private boolean hasPermission(HttpServletRequest request, List<GetProjectAuthorizationResponse> userPermission) {
        for(GetProjectAuthorizationResponse permission : userPermission){
            if(isMatchingPermission(request, permission)){
                return true;
            }
    }
        return false;
    }

    private boolean isMatchingPermission(HttpServletRequest request, GetProjectAuthorizationResponse permission) {
        String requestUri = request.getRequestURI();
        String requiredUri = "/api/v1/private/" + permission.getProjectId();

        
        if(requestUri.startsWith(requiredUri)){
            Long requiredAuthorityId = permission.getAuthorityId();
            switch (requiredAuthorityId.intValue()){
                case 1:
                    return permission.getAuthority().equals("admin");
                case 2:
                    return permission.getAuthority().equals("admin") || permission.getAuthorityId().equals("writer");
                case 3:
                    return true;
            }
        }
        return false;
    }
}