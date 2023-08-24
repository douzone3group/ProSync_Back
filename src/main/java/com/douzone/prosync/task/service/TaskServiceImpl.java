package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationData;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.notienum.NotificationPlatform;
import com.douzone.prosync.notification.repository.EmitterRepository;
import com.douzone.prosync.notification.repository.NotificationRepository;
import com.douzone.prosync.notification.service.NotificationService;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import com.douzone.prosync.task.dto.request.TaskPatchDto;
import com.douzone.prosync.task.dto.request.TaskPostDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.dto.response.GetTasksResponse;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.repository.TaskJpaRepository;
import com.douzone.prosync.task.repository.TaskMapper;
import com.douzone.prosync.task_status.service.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.douzone.prosync.constant.ConstantPool.FRONT_SERVER_HOST;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskJpaRepository taskJpaRepository;
    private final TaskStatusService taskStatusService;
    private final ProjectService projectService;

    private final MemberRepository memberRepository;

    private final NotificationRepository notificationRepository;

    private final NotificationService notificationService;


    @Override
    public Long createTask(TaskPostDto dto, Integer projectId, Long memberId) {
        // TODO : 프로젝트 회원 + writer인지 확인
        Project findProject = projectService.findProject(projectId);

        // check task_status of project
        verifyTaskStatus(findProject.getProjectId(), dto.getTaskStatusId(), memberId);

        dto.setCreatedAt(LocalDateTime.now());

        taskMapper.save(dto, projectId);
        return dto.getTaskId();
    }

    @Override
    public void updateTask(TaskPatchDto dto, Long taskId, Long memberId) {

        GetTaskResponse findTask = findExistTask(taskId);
        dto.setTaskId(taskId);
        dto.setModifiedAt(LocalDateTime.now());

        // find task_status of project
        if (Optional.ofNullable(dto.getTaskStatusId()).isPresent()) {
            verifyTaskStatus(findTask.getProjectId(), dto.getTaskStatusId(), memberId);
        }



        taskMapper.update(dto);
    }

    @Override
    public void deleteTask(Long taskId, Long memberId) {
        verifyExistTask(taskId);

        //soft delete
        taskMapper.delete(taskId);
    }

    @Transactional(readOnly = true)
    @Override
    public GetTaskResponse findTask(Long taskId, Long memberId) {
        return findExistTask(taskId);
    }

    /**
     * 업무 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<GetTasksResponse.PerTasksResponse> findTaskList(Integer projectId, Pageable pageable, String search, boolean isActive, Long memberId) {
        pageable = PageRequest.of(pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        Page<Task> pages = search != null && !search.trim().isEmpty() ?
                taskJpaRepository.findAllByProjectIdAndSearch(projectId, search, pageable) :
                taskJpaRepository.findByProjectIdAndIsDeletedNull(projectId, pageable);

        // isActive = true면 체크된 업무만 조회
        List<GetTasksResponse> res = isActive ? pages.getContent()
                .stream()
                .map(task -> GetTasksResponse.of(task))
                .filter(task -> task.getSeq() != 0)
                .collect(Collectors.toList()) :
                pages.getContent()
                .stream()
                .map(task -> GetTasksResponse.of(task))
                .collect(Collectors.toList());

        return new PageResponseDto(GetTasksResponse.PerTasksResponse.of(res), pages);
    }

    /**
     * 업무 담당자 지정
     */
    @Override
    public void createTaskMember(Long taskId, List<Long> memberIds, Long memberId) {
        // TODO : 프로젝트 회원 + writer 인지 검증
        // TODO : MEMBER_TASK 해당되는 값이 없을 경우 처리

        GetTaskResponse task = taskMapper.findById(taskId).orElse(null);
        if (task == null) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }

        taskMapper.saveTaskMember(taskId, memberIds);



        Member fromMember = memberRepository.findById(memberId).orElse(null);

        // DB에서 JOIN으로 들고오지 않고 데이터를 그대로 사용하기 위해 따로 선언했습니다.
        String content = fromMember.getEmail() + "님이 생성한 " + task.getTitle() + " 업무에 할당되셨습니다.";
        LocalDateTime date = LocalDateTime.now();
        String url = FRONT_SERVER_HOST + "/tasks/" + taskId;

        // 알림을 저장하고 pk 값 불러오기
        Long notificationId = notificationRepository.saveNotification(NotificationDto.builder()
                .code(NotificationCode.TASK_ASSIGNMENT)
                .fromMemberId(memberId)
                .createdAt(date)
                .content(content)
                .url(url).build());


        // 알림 타겟을 memberIds의 memberId와 알림 id를 이용하여 복수 저장하기
        List<NotificationTargetDto> dtoList = new ArrayList<>();

         memberIds.stream().map((id) ->  dtoList.add(
            NotificationTargetDto.builder()
                    .notificationId(notificationId)
                    .memberId(memberId)
                    .isRead(false)
                    .isTransmitted(false)
                    .platform(NotificationPlatform.WEB)
                    .createdAt(LocalDateTime.now()).build())
        );

         notificationRepository.saveNotificationTargetList(dtoList);

        List<NotificationTarget> notificationTargetList = notificationRepository.getNotificationTagetListByNotificationId(notificationId);

        notificationTargetList.stream().forEach((target) -> {
            NotificationResponse notification = new NotificationResponse(target.getNotificationId(),
                    target.isRead(), content, NotificationCode.TASK_ASSIGNMENT, date, url);

            try {
                notificationService.send((target.getMemberId()),new NotificationData(notification) );
                notificationRepository.updateIsTransmittedbyTagetId(true,target.getNotificationTargetId());
            } catch(RuntimeException e) {
                throw new ApplicationException(ErrorCode.CONNECTION_ERROR);
            }
         });



    }

    /**
     * 업무 담당자 삭제
     */
    @Override
    public void deleteTaskMember(Long taskId, List<Long> memberIds, Long memberId) {
        // TODO : 프로젝트 회원 + writer 인지 검증
        // TODO : MEMBER_TASK 해당되는 값이 없을 경우 처리
        GetTaskResponse task = taskMapper.findById(taskId).orElse(null);
        if (task == null) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }

        taskMapper.deleteTaskMember(taskId, memberIds);
    }

    private GetTaskResponse findExistTask(Long taskId) {
        return taskMapper.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 삭제된 task인 경우 예외 처리
     */
    private void verifyExistTask(Long taskId) {
        if (taskMapper.findExistsTask(taskId) == 0) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }
    }

    private void verifyTaskStatus(Integer projectId, Integer taskStatusId, Long memberId) {
        taskStatusService.getTaskStatusByProject(projectId, false, memberId)
                .stream()
                .filter(status -> status.getTaskStatusId() == taskStatusId).findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.TASK_STATUS_NOT_FOUND));
    }

}
