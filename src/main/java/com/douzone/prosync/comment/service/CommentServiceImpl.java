package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.repository.CommentMybatisMapper;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.file.dto.FileResponseDto;
import com.douzone.prosync.log.dto.LogConditionDto;
import com.douzone.prosync.log.logenum.LogCode;
import com.douzone.prosync.log.service.LogService;
import com.douzone.prosync.file.dto.FileRequestDto;
import com.douzone.prosync.file.entity.FileInfo;
import com.douzone.prosync.file.service.FileService;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.notification.dto.NotificationConditionDto;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.service.NotificationService;
import com.douzone.prosync.task.dto.TaskSimpleDto;
import com.douzone.prosync.task.dto.request.TaskMemberResponseDto;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.repository.TaskMapper;
import com.douzone.prosync.task.service.TaskServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.douzone.prosync.notification.notienum.NotificationCode.COMMENT_ADD;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {

    private final TaskServiceImpl taskService;

    private final TaskMapper taskMapper;

    private final CommentMybatisMapper commentMybatisMapper;

    private final MemberProjectService memberProjectService;

    private final NotificationService notificationService;

    private final LogService logService;

    private final FileService fileService;

    @Override
    public Long save(CommentPostDto dto) {
        findExistTask(dto);

        Long projectId = commentMybatisMapper.findProjectIdByTask(dto.getTaskId());
        MemberProjectResponseDto projectMember = memberProjectService.findProjectMember(projectId, dto.getMemberId());
        dto.setMemberProjectId(projectMember.getMemberProjectId());
        commentMybatisMapper.createComment(dto);
        Long commentId = dto.getCommentId();

        TaskSimpleDto taskDto = commentMybatisMapper.findTaskbyId(dto.getCommentId());


        // 해당 Task의 멤버들에게 알림을 전달하는 로직 작성
        List<TaskMemberResponseDto> taskMembers = taskMapper.findTaskMembers(taskDto.getTaskId());

        if (taskMembers.size() > 0) {
            List<Long> memberIds = taskMembers.stream()
                    .filter((taskMemberResponseDto) -> taskMemberResponseDto.getStatus().equals(MemberProject.MemberProjectStatus.ACTIVE))
                    .map(TaskMemberResponseDto::getMemberId)
                    .collect(Collectors.toList());

            if (memberIds.size() > 0) {
                // 알림 저장 및 전달
                notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                        .fromMemberId(dto.getMemberId())
                        .code(COMMENT_ADD)
                        .memberIds(memberIds)
                        .projectId(projectId)
                        .taskId(taskDto.getTaskId())
                        .subject(taskDto.getTitle()).build());
            }
        }


        // 로그 저장
        logService.saveLog(LogConditionDto.builder()
                .fromMemberId(dto.getMemberId())
                .code(LogCode.COMMENT_ADD)
                .projectId(taskDto.getProjectId())
                .taskId(taskDto.getTaskId())
                .subject(taskDto.getTitle()).build());


        if (!dto.getFileIds().isEmpty()) {
            List<FileInfo> fileInfos = FileInfo.createFileInfos(dto.getFileIds(), FileInfo.FileTableName.COMMENT, commentId);
            fileService.saveFileInfoList(fileInfos);
        }

        return commentId;
    }


    @Override
    public void update(CommentPatchDto dto, Long memberId) {
        verifyCommentMember(dto.getCommentId(), memberId);
        commentMybatisMapper.updateComment(dto);

        TaskSimpleDto taskDto = commentMybatisMapper.findTaskbyId(dto.getCommentId());


        // 해당 Task의 멤버들에게 알림을 전달하는 로직 작성
        List<TaskMemberResponseDto> taskMembers = taskMapper.findTaskMembers(taskDto.getTaskId());

        if (taskMembers.size() > 0) {
            List<Long> memberIds = taskMembers.stream()
                    .filter((taskMemberResponseDto) -> taskMemberResponseDto.getStatus().equals(MemberProject.MemberProjectStatus.ACTIVE))
                    .map(TaskMemberResponseDto::getMemberId)
                    .collect(Collectors.toList());

            if (memberIds.size() > 0) {
                // 알림 저장 및 전달
                notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                        .fromMemberId(memberId)
                        .code(NotificationCode.COMMENT_MODIFICATION)
                        .memberIds(memberIds)
                        .taskId(taskDto.getTaskId())
                        .projectId(taskDto.getProjectId())
                        .subject(taskDto.getTitle()).build());
            }
        }


        // 로그 저장
        logService.saveLog(LogConditionDto.builder()
                .fromMemberId(memberId)
                .code(LogCode.COMMENT_MODIFICATION)
                .projectId(taskDto.getProjectId())
                .taskId(taskDto.getTaskId())
                .subject(taskDto.getTitle()).build());

        if (!dto.getFileIds().isEmpty()) {
            List<FileInfo> fileInfos = FileInfo.createFileInfos(dto.getFileIds(), FileInfo.FileTableName.COMMENT, dto.getCommentId());
            fileService.saveFileInfoList(fileInfos);
        }
    }

    @Override
    public void delete(Long commentId, Long memberId) {
        verifyCommentMember(commentId, memberId);
        commentMybatisMapper.deleteComment(commentId);

        TaskSimpleDto taskDto = commentMybatisMapper.findTaskbyId(commentId);


        // 해당 Task의 멤버들에게 알림을 전달하는 로직 작성
        List<TaskMemberResponseDto> taskMembers = taskMapper.findTaskMembers(taskDto.getTaskId());

        if (taskMembers.size() > 0) {
            List<Long> memberIds = taskMembers.stream()
                    .filter((taskMemberResponseDto) -> taskMemberResponseDto.getStatus().equals(MemberProject.MemberProjectStatus.ACTIVE))
                    .map(TaskMemberResponseDto::getMemberId)
                    .collect(Collectors.toList());

            if (memberIds.size() > 0) {
                // 알림 저장 및 전달
                notificationService.saveAndSendNotification(NotificationConditionDto.builder()
                        .fromMemberId(memberId)
                        .code(NotificationCode.COMMENT_REMOVE)
                        .memberIds(memberIds)
                        .taskId(taskDto.getTaskId())
                        .projectId(taskDto.getProjectId())
                        .subject(taskDto.getTitle()).build());
            }


        }


        // 로그 저장
        logService.saveLog(LogConditionDto.builder()
                .fromMemberId(memberId)
                .code(LogCode.COMMENT_REMOVE)
                .projectId(taskDto.getProjectId())
                .taskId(taskDto.getTaskId())
                .subject(taskDto.getTitle()).build());

        fileService.deleteFileList(FileRequestDto.create(FileInfo.FileTableName.COMMENT, commentId));
    }

    @Override
    public PageResponseDto<GetCommentsResponse> findCommentList(Long taskId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GetCommentsResponse> comments = commentMybatisMapper.findAllComments(taskId);
        Gson gson = new Gson();
        comments.forEach(comment -> {
            List<FileResponseDto> dto = gson.fromJson(comment.getFiles(), new TypeToken<List<FileResponseDto>>() {}.getType());
            if (dto.get(0).getFileId() != null) {
                comment.setFileList(dto);
            }
            comment.setFiles(null);
        });
        PageInfo<GetCommentsResponse> pageInfo = new PageInfo<>(comments);

        return new PageResponseDto<>(pageInfo);
    }

    private MemberProjectResponseDto findCommentMember(Long commentId) {
        return commentMybatisMapper.findCommentMember(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.ACCESS_FORBIDDEN));
    }

    private void verifyCommentMember(Long commentId, Long memberId) {
        MemberProjectResponseDto commentMember = findCommentMember(commentId);
        if (commentMember.getMemberId() != memberId) {
            throw new ApplicationException(ErrorCode.ACCESS_FORBIDDEN);
        }
    }

    private void findExistTask(CommentPostDto dto) {
        GetTaskResponse findTask = taskService.findTask(dto.getTaskId(), dto.getMemberId());
        if (findTask == null) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }
    }
}