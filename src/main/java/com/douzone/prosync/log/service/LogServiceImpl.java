package com.douzone.prosync.log.service;


import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.log.dto.LogConditionDto;
import com.douzone.prosync.log.dto.LogDto;
import com.douzone.prosync.log.dto.request.LogPatchDto;
import com.douzone.prosync.log.dto.response.LogResponse;
import com.douzone.prosync.log.dto.response.LogSimpleResponse;
import com.douzone.prosync.log.repository.LogRepository;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.member_project.entity.MemberProject;
import com.douzone.prosync.member_project.repository.MemberProjectMapper;
import com.douzone.prosync.notification.dto.ContentUrlContainer;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.project.dto.request.ProjectPatchDto;
import com.douzone.prosync.project.dto.response.GetProjectResponse;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.project.service.ProjectService;
import com.douzone.prosync.searchcondition.LogSearchCondition;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.prosync.constant.ConstantPool.*;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final MemberRepository memberRepository;

    private final LogRepository logRepository;



    /**
     * LogCode에 따라 로그 생성 및 저장하는 로직
     */
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
                container.setContent(fromMember.getName() + "님이 " + ((GetTaskResponse) dto.getSubject()).getTitle() + " 업무를 삭제하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projectlog/"+dto.getProjectId());
            }
            break;
            case "업무지정": {

                List<Member> memberList = memberRepository.getMemberList(dto.getMemberIds());

                StringBuffer membersName = null;

                for (int i = 0; i < memberList.size(); i++) {
                    if (i == 0) {
                        membersName.append(memberList.get(i).getName());
                    } else {
                        membersName.append(", " + memberList.get(i).getName());
                    }
                }

                container.setContent(fromMember.getName() + "님이 " + membersName.toString() + "님을 " + ((GetTaskResponse) dto.getSubject()).getTitle() + " 업무로 배정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/tasks/" + dto.getTaskId());
            }
            break;
            case "업무수정": {
                container.setContent(fromMember.getName() + "님이 " + ((GetTaskResponse) dto.getSubject()).getTitle() + " 업무를 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/tasks/" + dto.getTaskId());
            }
            break;
            case "업무제외": {

                List<Member> memberList = memberRepository.getMemberList(dto.getMemberIds());

                StringBuffer membersName = null;

                for (int i = 0; i < memberList.size(); i++) {
                    if (i == 0) {
                        membersName.append(memberList.get(i).getName());
                    } else {
                        membersName.append(", " + memberList.get(i).getName());
                    }
                }

                container.setContent(fromMember.getName() + "님이 " + membersName.toString() + "님을 " + ((GetTaskResponse) dto.getSubject()).getTitle() + " 업무에서 제외하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/tasks/" + dto.getTaskId());
            }
            break;
            case "프로젝트지정": {
                container.setContent(fromMember.getName() + "님이 " + ((Project) dto.getSubject()).getTitle() + " 프로젝트의 구성원으로 수락하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projects/" + dto.getProjectId());
            }
            break;
            case "프로젝트제외": {


                container.setContent(fromMember.getName() + "님이 " + dto.getMemberId() + "님을 " + ((Project) dto.getSubject()).getTitle() + " 프로젝트의 구성원에서 제외하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projects/" + dto.getProjectId());
            }
            break;
            case "프로젝트탈퇴": {


                container.setContent(fromMember.getName() + "님이 " + ((Project) dto.getSubject()).getTitle() + " 프로젝트를 탈퇴하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projects/" + dto.getProjectId());
            }
            break;
            case "프로젝트수정": {

                container.setContent(fromMember.getName() + "님이 " + ((Project) dto.getSubject()).getTitle() + " 프로젝트에 대한 정보를 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projects/" + dto.getProjectId());
            }
            break;
            case "프로젝트삭제": {

                container.setContent(fromMember.getName() + "님이 " + ((Project) dto.getSubject()).getTitle() + " 프로젝트를 삭제하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projectlog/"+dto.getProjectId());
            }
            break;
            case "댓글추가": {
                container.setContent(fromMember.getName()+"님이 "+ dto.getSubject()+" 업무에 댓글을 추가하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+dto.getTaskId());
            }
            break;
            case "댓글삭제": {

                container.setContent(fromMember.getName()+"님이 "+ dto.getSubject()+" 업무에 댓글을 삭제하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+dto.getTaskId());
            }
            break;
            case "댓글수정": {

                container.setContent(fromMember.getName()+"님이 "+ dto.getSubject()+" 업무에 댓글을 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+dto.getTaskId());
            }
            break;
            case "권한변경": {

                // Todo : 회원_프로젝트에서 projectId로 ADMIN 권한을 가진 사용자를 찾아서 넣어줘야한다.
                Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

                if (dto.getAuthority().name().equals("ADMIN")) {
                    container.setContent("프로젝트의 관리자가 "+fromMember.getName()+"님에서 "+member.getName()+ "으로 변경되었습니다.");
                    container.setUrl(FRONT_SERVER_HOST+"/projects/" + dto.getProjectId());
                } else {
                    container.setContent(member.getName()+"님의 권한이 "+ dto.getAuthority().name()+"으로 변경되었습니다.");
                    container.setUrl(FRONT_SERVER_HOST+"/projects/" + dto.getProjectId());
                }
            }
            break;

        }

        Long logId = logRepository.saveLog(LogDto.builder()
                .logCode(code)
                .createdAt(container.getDate())
                .content(container.getContent())
                .isDeleted(false)
                .projectId(dto.getProjectId())
                .modifiedAt(container.getDate())
                .url(container.getUrl()).build());


        return new LogSimpleResponse(logId);
    }

        @Override
        public LogSimpleResponse deleteLog(Long logId){

            Long id = logRepository.deleteLog(logId);
            return new LogSimpleResponse(id);
        }


        @Override
        public PageInfo<LogResponse> getLogList(LogSearchCondition condition){
            return new PageInfo<>(logRepository.getLogList(condition), PAGE_NAVI);
        }

        @Override
        public LogSimpleResponse updateLog(Long logId,LogPatchDto dto){

            Long id = logRepository.updateLog(logId,dto);
            return new LogSimpleResponse(id);
        }

        @Override
        public Integer getLogListCount(Long projectId){
            return logRepository.getLogListCount(projectId);
        }

    @Override
    public PageResponseDto<LogResponse> getLogPageList(Long projectId, LogSearchCondition condition, Pageable pageable) {
        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();

        PageHelper.startPage(pageNum, pageable.getPageSize());

        LogSearchCondition logSearchCondition = condition.of(projectId, condition);

        List<LogResponse> logResponseList = logRepository.getLogList(logSearchCondition);

        PageInfo<LogResponse> pageInfo = new PageInfo<>(logResponseList);

        return new PageResponseDto<>(pageInfo);
    }


}
