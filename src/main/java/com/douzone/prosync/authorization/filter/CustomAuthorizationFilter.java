package com.douzone.prosync.authorization.filter;

import com.douzone.prosync.authorization.dto.GetProjectAuthorizationResponse;
import com.douzone.prosync.authorization.repository.ProjectAuthorizationRepository;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.exception.ErrorResponse;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.repository.MemberProjectMapper;
import com.douzone.prosync.member_project.status.ProjectMemberAuthority;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.repository.ProjectMapper;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.repository.TaskMapper;
import com.douzone.prosync.task_status.dto.TaskStatusDto;
import com.douzone.prosync.task_status.dto.TaskStatusDto.GetResponseDto;
import com.douzone.prosync.task_status.repository.TaskStatusMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.douzone.prosync.member_project.status.ProjectMemberAuthority.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {


    private final ProjectAuthorizationRepository authorizationRepository;

    private final ProjectMapper projectMapper;

    private final MemberProjectMapper memberProjectMapper;

    private final TaskMapper taskMapper;

    private final TaskStatusMapper taskStatusMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            filterChain.doFilter(request, response);
        } else {
            Long memberId = getIdFromPrincipal(authentication);

            String requestUri = request.getRequestURI().substring(8);
            String httpMethod = request.getMethod();

            try{

                accessPermissionCheck(requestUri, httpMethod, memberId);

            } catch (ApplicationException e){
                ObjectMapper mapper = new ObjectMapper();
                String jsonInString = mapper.writeValueAsString(ErrorResponse.of(e.getErrorCode().name()));

                response.setContentType("application/json");
                response.setStatus(e.getErrorCode().getStatus().value());
                response.getWriter().write(jsonInString);
                return;
            }

            filterChain.doFilter(request, response);

        }


    }
    /**
     * principal에서 memberid 가져오기
     */
    Long getIdFromPrincipal(Authentication authentication) {
        Long memberId = Long.parseLong(authentication.getName());
        log.debug("{}", memberId);

        return memberId;
    }

    private void accessPermissionCheck(String requestUri, String httpMethod, Long memberId){

        switch (httpMethod) {

            case "GET": {
                if (requestUri.equals("my-projects") || requestUri.equals("projects")) {
                    log.debug("{}", "[GET] 1");
                    return;
                }

                if (requestUri.matches("projectlog/.*") || requestUri.matches("projects/.*/invitation")) {
                    log.debug("{}", "[GET] 2");

                    String[] uriList = requestUri.split("/");
                    Long projectId = Long.parseLong(uriList[1]);

                    checkAdminByProjectId(memberId, projectId);
                    return;
                }

               if (requestUri.startsWith("projects")) {
                   log.debug("{}", "[GET] 3");
                   String[] uriList = requestUri.split("/");
                   Long projectId = Long.parseLong(uriList[1]);

                   checkProjectPublic(memberId, projectId);
                   return;
                }

               if (requestUri.startsWith("tasks")) {
                   log.debug("{}", "[GET] 4");

                   String[] uriList = requestUri.split("/");
                   Long taskId = Long.parseLong(uriList[1]);

                   GetTaskResponse getTaskResponse = taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
                   Long projectId = getTaskResponse.getProjectId();

                   checkProjectPublic(memberId, projectId);
                   return;
               }

               if (requestUri.startsWith("task-status")) {
                   log.debug("{}", "[GET] 5");

                   String[] uriList = requestUri.split("/");
                   Long taskStatusId = Long.parseLong(uriList[1]);

                   Long projectId = getProjectIdByTaskStatusId(taskStatusId);

                   checkProjectPublic(memberId, projectId);
                   return;
               }

               else {
                   System.out.println("통과");
               }

            }
            break;

            case "POST": {
                if (requestUri.equals("projects")) {
                    log.debug("{}", "[POST] 1");

                    return;
                }

                if (requestUri.startsWith("projects")) {
                    log.debug("{}", "[POST] 2");

                    String[] uriList = requestUri.split("/");
                    Long projectId = Long.parseLong(uriList[1]);

                    checkWriterOrAdminByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("tasks")) {
                    log.debug("{}", "[POST] 3");
                    String[] uriList = requestUri.split("/");
                    Long taskId = Long.parseLong(uriList[1]);

                    GetTaskResponse getTaskResponse = taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
                    Long projectId = getTaskResponse.getProjectId();

                    checkWriterOrAdminByProjectId(memberId, projectId);
                    return;
                }

//                if (requestUri.startsWith("files")) {  // 보류
//
//                }

            }

            break;

            case "PATCH":{
                if(requestUri.startsWith("projectlog") || requestUri.startsWith("projects")){
                    log.debug("{}", "[PATCH] 1");
                    String[] uriList = requestUri.split("/");
                    Long projectId = Long.parseLong(uriList[1]);

                    checkAdminByProjectId(memberId, projectId);
                    return;
                }

                if(requestUri.startsWith("project-members")){
                    log.debug("{}", "[PATCH] 2");
                    String[] uriList = requestUri.split("/");
                    Long projectMemberId = Long.parseLong(uriList[1]);

                    Long projectId = memberProjectMapper.findProjectByProjectMemberId(projectMemberId);
                    checkAdminByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("tasks")) {
                    log.debug("{}", "[PATCH] 3");
                    String[] uriList = requestUri.split("/");
                    Long taskId = Long.parseLong(uriList[1]);

                    GetTaskResponse getTaskResponse = taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));

                    Long projectId = getTaskResponse.getProjectId();

                    checkWriterOrAdminByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("task-status")) {
                    log.debug("{}", "[PATCH] 4");
                    String[] uriList = requestUri.split("/");
                    Long taskStatusId = Long.parseLong(uriList[1]);

                    Long projectId = getProjectIdByTaskStatusId(taskStatusId);

                    checkWriterOrAdminByProjectId(memberId, projectId);
                    return;
                }


            }
            break;

            case "DELETE":{

                if (requestUri.matches("projects/.*/members")) {
                    log.debug("{}", "[DELETE] 1");
                    String[] uriList = requestUri.split("/");
                    Long projectId= Long.parseLong(uriList[1]);

                    checkProjectMemberByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("tasks")) {
                    log.debug("{}", "[DELETE] 2");
                    String[] uriList = requestUri.split("/");
                    Long taskId = Long.parseLong(uriList[1]);

                    GetTaskResponse getTaskResponse = taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));

                    Long projectId = getTaskResponse.getProjectId();

                    checkWriterOrAdminByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("task-status")) {
                    log.debug("{}", "[DELETE] 3");
                    String[] uriList = requestUri.split("/");
                    Long taskStatusId = Long.parseLong(uriList[1]);

                    Long projectId = getProjectIdByTaskStatusId(taskStatusId);

                    checkWriterOrAdminByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("projectlog") || requestUri.startsWith("projects")) {
                    log.debug("{}", "[DELETE] 4");
                    String[] uriList = requestUri.split("/");
                    Long projectId = Long.parseLong(uriList[1]);

                    checkAdminByProjectId(memberId, projectId);
                    return;
                }

                if (requestUri.startsWith("project-members")) {
                    log.debug("{}", "[DELETE] 5");
                    String[] uriList = requestUri.split("/");
                    Long projectMemberId = Long.parseLong(uriList[1]);

                    Long projectId = memberProjectMapper.findProjectByProjectMemberId(projectMemberId);
                    checkAdminByProjectId(memberId, projectId);
                    return;
                }
            }
            break;

        }
    }

    private Long getProjectIdByTaskStatusId(Long taskStatusId) {
        GetResponseDto getResponseDto = taskStatusMapper.findTaskStatus(taskStatusId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));

        Long projectId = getResponseDto.getProjectId();
        return projectId;
    }

    private void checkProjectMemberByProjectId(Long memberId, Long projectId) {
        GetProjectAuthorizationResponse authorizationResponse = authorizationRepository.getUserAuthorization(memberId, projectId)
                .orElseThrow(()-> new ApplicationException(ErrorCode.MEMBER_NOT_INCLUDED_IN_PROJECT));

        if(authorizationResponse.getStatus().equals(MemberProject.MemberProjectStatus.QUIT.name())){
            throw new ApplicationException(ErrorCode.MEMBER_NOT_INCLUDED_IN_PROJECT);
        }
    }

    private void checkWriterOrAdminByProjectId(Long memberId, Long projectId) {
        GetProjectAuthorizationResponse authorizationResponse = authorizationRepository.getUserAuthorization(memberId, projectId)
                .orElseThrow(()-> new ApplicationException(ErrorCode.MEMBER_NOT_INCLUDED_IN_PROJECT));

        if(!authorizationResponse.getAuthority().equals(ADMIN.name()) && !authorizationResponse.getAuthority().equals(WRITER.name())){
            throw new ApplicationException(ErrorCode.INAPPROPRIATE_PERMISSION);
        }
    }

    private void checkProjectPublic(Long memberId, Long projectId) {
        Project project = projectMapper.findById(projectId).orElseThrow(() -> new ApplicationException(ErrorCode.PROJECT_NOT_FOUND));

        if(!project.getIsPublic()){
            MemberProjectResponseDto memberProjectResponseDto = memberProjectMapper.findProjectMember(projectId, memberId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.MEMBER_NOT_INCLUDED_IN_PROJECT));
            if(memberProjectResponseDto.getStatus().equals(MemberProject.MemberProjectStatus.QUIT)){
                 throw new ApplicationException(ErrorCode.MEMBER_NOT_INCLUDED_IN_PROJECT);
            }
        }
    }

    private void checkAdminByProjectId(Long memberId, Long projectId) {
        GetProjectAuthorizationResponse authorizationResponse = authorizationRepository.getUserAuthorization(memberId, projectId)
                .orElseThrow(()-> new ApplicationException(ErrorCode.MEMBER_NOT_INCLUDED_IN_PROJECT));

        if(!authorizationResponse.getAuthority().equals(ADMIN.name())){
            throw new ApplicationException(ErrorCode.INAPPROPRIATE_PERMISSION);
        }
    }

}