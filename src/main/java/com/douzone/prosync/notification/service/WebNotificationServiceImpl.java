package com.douzone.prosync.notification.service;

import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.notification.NoEmitterException;
import com.douzone.prosync.notification.dto.ContentUrlContainer;
import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.response.NotificationData;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.dto.response.NotificationTargetSimpleResponse;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.notienum.NotificationPlatform;
import com.douzone.prosync.notification.repository.MapEmitterRepository;
import com.douzone.prosync.notification.repository.MybatisNotificationRepository;
import com.douzone.prosync.project.dto.response.GetProjectResponse;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.douzone.prosync.constant.ConstantPool.*;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WebNotificationServiceImpl implements NotificationService{

    private final MapEmitterRepository emitterRepositoryrepository;
    private final MybatisNotificationRepository notificationRepository;

    private final MemberRepository memberRepository;

    /**
     * 서버에서 클라이언트로 data 전송
     */
    private void sendToClient(SseEmitter sseEmitter, Object data) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            System.out.println("SSE 연결 에러 발생");
            throw new RuntimeException("SSE 연결 오류 발생");
        }
    }


    /**
     * pk에 해당하는 사용자와 서버 간의 구독
     */
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = emitterRepositoryrepository.save(memberId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepositoryrepository.deleteById(memberId));
        emitter.onTimeout(() -> emitterRepositoryrepository.deleteById(memberId));
        emitter.onError((e) -> emitterRepositoryrepository.deleteById(memberId));

        Integer count = notificationRepository.getNotificationListCount(memberId);

        try {
            sendToClient(emitter,new NotificationData("안읽으신 "+count+"개의 메시지가 있습니다.",FRONT_SERVER_HOST+"/notifications"));
            notificationRepository.updateIsTransmittedbyMemberId(true,memberId);
        } catch (RuntimeException e){
            throw new ApplicationException(ErrorCode.CONNECTION_ERROR);
        }

        return emitter;
    }


    /**
     * 사용자 pk에 해당하는 SseEmitter를 찾아서 data를 전송한다.
     */
    @Override
    public void send(Long memberId, Object data) {
        SseEmitter emitter = emitterRepositoryrepository.findById(memberId);
        if (emitter==null) {
            throw new NoEmitterException();
        }
        sendToClient(emitter,data);

    }

    /**
     * 알림을 저장하고 알림 수취인이 Sse 연결이 된 상태이면 알림을 전송한다.
     * @param fromMemberId 알림 제공자(알림을 뜨게 한 사용자)
     * @param code 알림의 종류(코드)
     * @param subject 프로젝트나 업무에 관련된 객체 자료
     * @param memberIds 알림의 수취인 집단
     */
    @Override
    public void saveAndSendNotification(Long fromMemberId, NotificationCode code, Object subject, List<Long> memberIds) {

        // 알림의 공통 속성인 code, content, url, date를 code의 분류에 따라 매핑시킨다.
        // 람다식 사용을 위해 container에 속성값들을 매핑시켰다.
        ContentUrlContainer container = new ContentUrlContainer();

        LocalDateTime date = LocalDateTime.now();
        container.setDate(date);

        Member fromMember = memberRepository.findById(fromMemberId).orElse(null);


        switch (code.getCode()) {
            case "업무삭제": {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무를 삭제하셨습니다.") ;
                container.setUrl(FRONT_SERVER_HOST+"/notifications");
            }
                break;
            case "업무지정":   {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무로 배정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) subject).getTaskId());
            }
                break;
            case "업무수정":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무를 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) subject).getTaskId());
            }
                break;
            case "업무제외":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무에서 제외하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) subject).getTaskId());
            }
                break;
            case "프로젝트지정":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetProjectResponse) subject).getName()+" 프로젝트의 구성원으로 수락하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/projects/"+((GetProjectResponse) subject).getProjectId());
            }
                break;
            case "프로젝트제외":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetProjectResponse) subject).getName()+" 프로젝트의 구성원에서 제외하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/notifications");
            }
                break;
            case "프로젝트권한변경":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetProjectResponse) subject).getName()+" 프로젝트에 대한 권한을 변경하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/projects/"+((GetProjectResponse) subject).getProjectId());
            }
                break;
            case "프로젝트수정":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetProjectResponse) subject).getName()+" 프로젝트에 대한 정보를 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST + "/projects/" + ((GetProjectResponse) subject).getProjectId());
            }
                break;
            case "프로젝트삭제":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetProjectResponse) subject).getName()+" 프로젝트를 삭제하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/notifications");
            }
                break;
            case "댓글추가":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무에 댓글을 추가하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) subject).getTaskId());
            }
                break;
            case "댓글삭제":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무에 댓글을 삭제하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) subject).getTaskId());
            }
                break;
            case "댓글수정":  {
                container.setContent(fromMember.getEmail()+"님이 "+ ((GetTaskResponse) subject).getTitle()+" 업무에 댓글을 수정하셨습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/tasks/"+((GetTaskResponse) subject).getTaskId());
            }
                break;
            case "관리자변경":  {
                //Todo : 회원_프로젝트에서 projectId로 ADMIN 권한을 가진 사용자를 찾아서 넣어줘야한다.
                container.setContent("프로젝트의 관리자가 "+fromMember.getEmail()+"님에서 "+ "으로 변경되었습니다.");
                container.setUrl(FRONT_SERVER_HOST+"/projects/" + ((GetProjectResponse) subject).getProjectId());
            }
            break;

        }

        // 알림을 저장하고 pk 값 불러온다.
        Long notificationId = notificationRepository.saveNotification(NotificationDto.builder()
                .code(code.getCode())
                .fromMemberId(fromMemberId)
                .createdAt(date)
                .content(container.getContent())
                .url(container.getUrl())
                .build());

        // 알림 타겟을 memberIds의 memberId와 알림 id를 이용하여 복수로 저장한다.
        List<NotificationTargetDto> dtoList = new ArrayList<>();

        memberIds.stream().forEach((id) -> dtoList.add(
                NotificationTargetDto.builder()
                        .notificationId(notificationId)
                        .memberId(id)
                        .isRead(false)
                        .isTransmitted(false)
                        .platform(NotificationPlatform.WEB)
                        .createdAt(date).
                        updateUserId(fromMemberId)
                        .build()

        ));
        System.out.println("memberIds"+memberIds.toString());
        System.out.println("dtoList"+dtoList.toString());
        notificationRepository.saveNotificationTargetList(dtoList);

        // 알림 id에 해당하는 알림 타겟을 꺼내와서 for문을 돌며 수취인의 memberId에 해당하는 SseEmitter를 통해 알림을 전송한다.
        List<NotificationTarget> notificationTargetList = notificationRepository.getNotificationTagetListByNotificationId(notificationId);

        notificationTargetList.stream().forEach((target) -> {
            NotificationResponse notification = new NotificationResponse(target.getNotificationId(),
                    target.isRead(), container.getContent(), code, container.getDate(), container.getUrl());

            try {
                send((target.getMemberId()),new NotificationData(notification));
                notificationRepository.updateIsTransmittedbyTagetId(true,target.getNotificationTargetId());
            } catch(NoEmitterException e) {
                log.debug("{}",target.getMemberId()+"은 접속중이 아닙니다");
            }catch (RuntimeException e) {
                throw new ApplicationException(ErrorCode.CONNECTION_ERROR);
            }
        });

    }


    /**
     * 알림을 읽음 처리로 업데이트하는 로직
     * @param targetId NotificationTarget의 pk
     * @param memberId 알림 수취인 pk
     * @return
     */
    @Override
    public NotificationTargetSimpleResponse updateNotificationIsRead(Long targetId, Long memberId) {

        // 알림 검증 로직(DB에 알림이 존재하는지와 본인에 대한 알림이 맞는지)
        NotificationTarget target = notificationRepository.findTargetById(targetId);
        if (target==null) {
            throw new ApplicationException(ErrorCode.NOTIFICATION_NOT_FOUND);
        } else if (!target.getMemberId().equals(memberId)) {
            throw new ApplicationException(ErrorCode.NOTIFICATION_CANT_READ);
        }

        notificationRepository.updateIsRead(true, targetId);
        return new NotificationTargetSimpleResponse(targetId);
    }
}
