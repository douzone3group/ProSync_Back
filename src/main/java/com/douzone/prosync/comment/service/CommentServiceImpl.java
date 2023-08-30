package com.douzone.prosync.comment.service;

import com.douzone.prosync.comment.dto.request.CommentPatchDto;
import com.douzone.prosync.comment.dto.request.CommentPostDto;
import com.douzone.prosync.comment.dto.response.GetCommentsResponse;
import com.douzone.prosync.comment.repository.CommentMybatisMapper;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member_project.dto.MemberProjectResponseDto;
import com.douzone.prosync.member_project.service.MemberProjectService;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.douzone.prosync.task.service.TaskServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl  implements CommentService{

    private final TaskServiceImpl taskService;

    private final CommentMybatisMapper commentMybatisMapper;

    private final MemberProjectService memberProjectService;

    @Override
    public Long save(CommentPostDto dto) {
        findExistTask(dto);

        Long projectId = commentMybatisMapper.findProjectIdByTask(dto.getTaskId());
        MemberProjectResponseDto projectMember = memberProjectService.findProjectMember(projectId, dto.getMemberId());
        dto.setMemberProjectId(projectMember.getMemberProjectId());
        commentMybatisMapper.createComment(dto);

        return dto.getCommentId();
    }


    @Override
    public void update(CommentPatchDto dto, Long memberId) {
        verifyCommentMember(dto.getCommentId(), memberId);
        commentMybatisMapper.updateComment(dto);
    }

    @Override
    public void delete(Long commentId, Long memberId) {
        verifyCommentMember(commentId, memberId);
        commentMybatisMapper.deleteComment(commentId);
    }

    @Override
    public PageResponseDto<GetCommentsResponse> findCommentList(Long taskId, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());

        List<GetCommentsResponse> comments = commentMybatisMapper.findAllComments(taskId);
        PageInfo<GetCommentsResponse> pageInfo = new PageInfo<>(comments);

        comments.forEach(comment -> comment.setMemberInfo(findCommentMember(comment.getCommentId())));

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
