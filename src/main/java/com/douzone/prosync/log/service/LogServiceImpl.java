package com.douzone.prosync.log.service;


import com.douzone.prosync.log.dto.LogConditionDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.log.dto.response.LogSimpleResponse;
import com.douzone.prosync.log.repository.LogRepository;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.notification.dto.ContentUrlContainer;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

import static com.douzone.prosync.constant.ConstantPool.*;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final MemberRepository memberRepository;

    private final LogRepository logRepository;

    // TODO: 로그 저장하는 로직 작성하기
    @Override
    public LogSimpleResponse saveLog(LogConditionDto dto) {

        String code = dto.getCode().getCode();

        // 로그의 공통 속성인 code, content, url, date를 code의 분류에 따라 매핑시킨다.
        // 람다식 사용을 위해 container에 속성값들을 매핑시켰다.
        ContentUrlContainer container = new ContentUrlContainer();

        LocalDateTime date = LocalDateTime.now();
        container.setDate(date);

        Member fromMember = memberRepository.findById(dto.getFromMemberId()).orElse(null);

        switch (code) {
            case "업무삭제": {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" 업무를 삭제하셨습니다.") ;
                container.setUrl(FRONT_SERVER_HOST+"/notifications");
            }
            break;
            case "업무지정":   {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" 업무로 배정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) dto.getSubject()).getTaskId());
            }
            break;
            case "업무수정":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" 업무를 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) dto.getSubject()).getTaskId());
            }
            break;
            case "업무제외":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" 업무에서 제외하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) dto.getSubject()).getTaskId());
            }
        }

        return new LogSimpleResponse(1L);
    }

    @Override
    public LogSimpleResponse deleteLog(Long logId) {

        Long id = logRepository.deleteLog(logId);
        return new LogSimpleResponse(id);
    }

    @Override
    public PageInfo<LogResponse> getLogList(LogSearchCondition condition) {

        return new PageInfo<>(logRepository.getLogList(condition), PAGE_NAVI);

    }

    @Override
    public LogSimpleResponse updateLog(LogPatchDto dto) {

        Long id = logRepository.updateLog(dto);
        return new LogSimpleResponse(id);
    }

    @Override
    public Integer getLogListCount(Long projectId) {
        return logRepository.getLogListCount(projectId);
    }
}
