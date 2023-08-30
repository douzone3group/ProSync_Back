package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member.dto.response.MemberGetResponse;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.service.NotificationService;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import com.douzone.prosync.task.repository.TaskMapper;
import com.douzone.prosync.task_status.service.TaskStatusService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskStatusService taskStatusService;
    private final ProjectService projectService;
    private final MemberProjectService memberProjectService;



    private final NotificationService notificationService;


    @Override
    public Long createTask(TaskPostDto dto, Long projectId, Long memberId) {
        Project findProject = projectService.findProject(projectId);

        // check task_status of project
        verifyTaskStatus(findProject.getProjectId(), dto.getTaskStatusId(), memberId);

        taskMapper.save(dto, projectId);
        return dto.getTaskId();
    }

    @Override
    public void updateTask(TaskPatchDto dto, Long taskId, Long memberId) {

        GetTaskResponse findTask = findExistTask(taskId);
        dto.setTaskId(taskId);

        // find task_status of project
        if (Optional.ofNullable(dto.getTaskStatusId()).isPresent()) {
            verifyTaskStatus(findTask.getProjectId(), dto.getTaskStatusId(), memberId);
        }

        taskMapper.update(dto);
    }

    @Override
    public void deleteTask(Long taskId, Long memberId) {
        GetTaskResponse task = verifyExistTask(taskId);
        //soft delete
        taskMapper.delete(taskId);


        // Todo: 해당 Task의 멤버들에게 알림을 전달하는 로직 작성
//        taskMapper.
//        notificationService.saveAndSendNotification(memberId, NotificationCode.TASK_REMOVE, task, );
    }

    @Transactional(readOnly = true)
    @Override
    public GetTaskResponse findTask(Long taskId, Long memberId) {
        return findExistTask(taskId);
    }

    /**
     * 업무 리스트 조회
     */
    @Override
    public PageResponseDto<GetTasksResponse.PerTasksResponse> findTaskList(Long projectId, Pageable pageable, String search, boolean isActive, String view, String status, Long memberId) {
        pageable = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());

        List<GetTasksResponse> tasks = taskMapper.findTasks(projectId, search, isActive);

        // task member 세팅
        tasks.forEach(task -> {
            task.setTaskMembers(taskMapper.findTaskMembers(task.getTaskId()));
        });

        // 필터 - task status
        if (status != null && !status.trim().isEmpty()) {
            tasks = tasks.stream().filter(task -> task.getTaskStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
        }

        // 보드뷰일 경우 task_status별 응답 리턴
        if (view != null && view.equals("board")) {
            List<GetTasksResponse.PerTasksResponse> list = GetTasksResponse.PerTasksResponse.of(tasks);
            return new PageResponseDto<>(new PageInfo<>(list));
        }
        return new PageResponseDto(new PageInfo<>(tasks));
    }

    /**
     * 업무 담당자 지정
     */
    @Override
    public void createTaskMember(Long taskId, List<Long> memberIds, Long memberId) {

        GetTaskResponse task = verifyExistTask(taskId);

        // 업무 담당자 중복 추가할 경우 예외
        List<MemberGetResponse.SimpleResponse> taskMembers = taskMapper.findTaskMembers(taskId);
        taskMembers.forEach(taskMember -> {
            if (memberIds.contains(taskMember.getMemberId())) {
                throw new ApplicationException(ErrorCode.TASK_MEMBER_EXISTS);
            }
        });

        taskMapper.saveTaskMember(taskId, memberIds);

        notificationService.saveAndSendNotification(memberId, NotificationCode.TASK_ASSIGNMENT,
                task, memberIds);

    }

    /**
     * 업무 담당자 삭제
     */
    @Override
    public void deleteTaskMember(Long taskId, List<Long> memberIds, Long memberId) {
        GetTaskResponse task = verifyExistTask(taskId);

        taskMapper.deleteTaskMember(taskId, memberIds);

        notificationService.saveAndSendNotification(memberId, NotificationCode.TASK_EXCLUDED, task, memberIds);
    }

    @Override
    public List<MemberGetResponse.SimpleResponse> findTaskMembers(Long taskId, long memberId) {
        return taskMapper.findTaskMembers(taskId);
    }

    private GetTaskResponse findExistTask(Long taskId) {
        return taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 삭제된 task인 경우 예외 처리
     */
    private GetTaskResponse verifyExistTask(Long taskId) {

        GetTaskResponse task = taskMapper.findById(taskId).orElse(null);
        if (task == null) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }

        return task;
    }

    private void verifyTaskStatus(Long projectId, Long taskStatusId, Long memberId) {
        taskStatusService.getTaskStatusByProject(projectId, false, memberId)
                .stream()
                .filter(status -> status.getTaskStatusId() == taskStatusId).findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }

}
