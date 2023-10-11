package com.douzone.prosync.notification.service;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.member.entity.Member;
import com.douzone.prosync.member.repository.MemberRepository;
import com.douzone.prosync.notification.NoEmitterException;
import com.douzone.prosync.notification.dto.ContentUrlContainer;
import com.douzone.prosync.notification.dto.NotificationConditionDto;
import com.douzone.prosync.notification.dto.NotificationDto;
import com.douzone.prosync.notification.dto.NotificationTargetDto;
import com.douzone.prosync.notification.dto.request.NotificationListRequestDto;
import com.douzone.prosync.notification.dto.response.NotificationData;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.dto.response.NotificationTargetSimpleResponse;
import com.douzone.prosync.notification.entity.NotificationTarget;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.notienum.NotificationPlatform;
import com.douzone.prosync.notification.repository.MapEmitterRepository;
import com.douzone.prosync.notification.repository.MybatisNotificationRepository;
import com.douzone.prosync.project.entity.Project;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import com.douzone.prosync.task.dto.response.GetTaskResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.douzone.prosync.constant.ConstantPool.*;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WebNotificationServiceImpl implements NotificationService{

    private final MapEmitterRepository emitterRepository;
    private final MybatisNotificationRepository notificationRepository;

    private final MemberRepository memberRepository;


    private final NotificationMapper mapper;

    /**
     * 서버에서 클라이언트로 data 전송
     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendToClient(SseEmitter sseEmitter, Object data) {

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            System.out.println("SSE 전송 오류 발생");
            throw new ApplicationException(ErrorCode.CONNECTION_ERROR,"SSE 전송 오류 발생");
        }
    }


    /**
     * pk에 해당하는 사용자와 서버 간의 구독
     */
    public SseEmitter subscribe(Long memberId) {

        if (memberRepository.findById(memberId).isEmpty()) {
            throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        }

        SseEmitter emitter;
        Integer count;

        try {
            emitter = emitterRepository.save(memberId, new SseEmitter(DEFAULT_TIMEOUT));

            emitter.onCompletion(() -> emitterRepository.deleteById(memberId));
            emitter.onTimeout(() -> emitterRepository.deleteById(memberId));
            emitter.onError((e) -> emitterRepository.deleteById(memberId));

            count = mapper.getNotificationCountIsReadFalse(memberId);
        } catch (RuntimeException e) {
            System.out.println("SSE 구독 오류 발생");
            throw new ApplicationException(ErrorCode.CONNECTION_ERROR, "SSE 구독 오류 발생");
        }


        sendToClient(emitter,new NotificationData("안읽으신 "+count+"개의 메시지가 있습니다.",FRONT_SERVER_HOST+"/notification"));
        notificationRepository.updateIsTransmittedbyMemberId(true,memberId);



        return emitter;
    }


    /**
     * 사용자 pk에 해당하는 SseEmitter를 찾아서 data를 전송한다.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void send(Long memberId, Object data) {
        SseEmitter emitter = emitterRepository.findById(memberId);
        if (emitter==null) {
            throw new NoEmitterException();
        }
        sendToClient(emitter,data);

    }


    /**
     * 알림을 저장하고 알림 수취인이 Sse 연결이 된 상태이면 알림을 전송한다.
     * @param dto 알림에 대한 조건이 들어있는 객체
     */
    @Override
    public void saveAndSendNotification(NotificationConditionDto dto) {

        // 알림의 공통 속성인 code, content, url, date를 code의 분류에 따라 매핑시킨다.
        // 람다식 사용을 위해 container에 속성값들을 매핑시켰다.
        ContentUrlContainer container = new ContentUrlContainer();

        LocalDateTime date = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formattedDateTime = date.format(formatter);
        container.setDate(formattedDateTime);

        Member fromMember = memberRepository.findById(dto.getFromMemberId()).orElse(null);
        NotificationCode code = dto.getCode();


        switch (code.getCode()) {
            case "업무삭제": {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" ] 업무를 삭제하셨습니다.") ;
                container.setUrl("/notification");
            }
                break;
            case "업무지정":   {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" ] 업무로 배정하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId()+"/tasks/"+dto.getTaskId());
            }
                break;
            case "업무수정":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" ] 업무를 수정하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId()+"/tasks/"+dto.getTaskId());
            }
                break;
            case "업무제외":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((GetTaskResponse) dto.getSubject()).getTitle()+" ] 업무에서 제외하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId()+"/tasks/"+dto.getTaskId());
            }
                break;
            case "프로젝트지정":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((Project) dto.getSubject()).getTitle()+" ] 프로젝트의 구성원으로 수락하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId());
            }
                break;
            case "프로젝트제외":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((Project) dto.getSubject()).getTitle()+" ] 프로젝트의 구성원에서 제외하셨습니다.");
                container.setUrl("/notification");
            }
                break;
            case "프로젝트수정":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((Project) dto.getSubject()).getTitle()+" ] 프로젝트에 대한 정보를 수정하셨습니다.");
                container.setUrl("/projects/" + dto.getProjectId());
            }
                break;
            case "프로젝트삭제":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ ((Project) dto.getSubject()).getTitle()+" ] 프로젝트를 삭제하셨습니다.");
                container.setUrl("/notification");
            }
                break;
            case "댓글추가":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ dto.getSubject()+" ] 업무에 댓글을 추가하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId()+"/tasks/"+dto.getTaskId());
            }
                break;
            case "댓글삭제":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ dto.getSubject()+" ] 업무에 댓글을 삭제하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId()+"/tasks/"+dto.getTaskId());
            }
                break;
            case "댓글수정":  {
                container.setContent("[ "+fromMember.getNameEmail()+" ] 님이 [ "+ dto.getSubject()+" ] 업무에 댓글을 수정하셨습니다.");
                container.setUrl("/projects/"+dto.getProjectId()+"/tasks/"+dto.getTaskId());
            }
                break;
            case "권한변경":  {
//                Todo : 회원_프로젝트에서 projectId로 ADMIN 권한을 가진 사용자를 찾아서 넣어줘야한다.

                Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

                if (dto.getAuthority().name().equals("ADMIN")) {
                    container.setContent("[ "+((Project)dto.getSubject()).getTitle() +" ] 프로젝트의 관리자가 [ "+fromMember.getNameEmail()+" ] 님에서 [ "+member.getNameEmail()+ " ] 으로 변경되었습니다.");
                    container.setUrl("/projects/" + dto.getProjectId());
                } else {
                    container.setContent("[ "+((Project)dto.getSubject()).getTitle()+" ] 프로젝트에서 [ "+member.getNameEmail()+" ] 님의 권한이 [ "+ dto.getAuthority().name()+" ] 으로 변경되었습니다.");
                    container.setUrl("/projects/" + dto.getProjectId());
                }

            }
            break;

        }

        // 알림을 저장하고 pk 값 불러온다.
        Long notificationId = notificationRepository.saveNotification(NotificationDto.builder()
                .code(code)
                .fromMemberId(dto.getFromMemberId())
                .createdAt(date)
                .content(container.getContent())
                .url(container.getUrl())
                .build());

        // 알림 타겟을 memberIds의 memberId와 알림 id를 이용하여 복수로 저장한다.
        List<NotificationTargetDto> dtoList = new ArrayList<>();

        System.out.println("알림 보낼 멤버 리스트:"+dto.getMemberIds());

        dto.getMemberIds().stream().forEach((id) -> dtoList.add(
                NotificationTargetDto.builder()
                        .notificationId(notificationId)
                        .memberId(id)
                        .isRead(false)
                        .isTransmitted(false)
                        .platform(NotificationPlatform.WEB)
                        .createdAt(date).
                        updateUserId(dto.getFromMemberId())
                        .build()

        ));



        notificationRepository.saveNotificationTargetList(dtoList);

        // 알림 id에 해당하는 알림 타겟을 꺼내와서 for문을 돌며 수취인의 memberId에 해당하는 SseEmitter를 통해 알림을 전송한다.
        List<NotificationTarget> notificationTargetList = notificationRepository.getNotificationTargetListByNotificationId(notificationId);


        notificationTargetList.stream().forEach((target) -> {
            NotificationResponse notification = new NotificationResponse(target.getNotificationId(),
                    target.isRead(), container.getContent(), code, container.getDate().toString(), container.getUrl());

            try {
                System.out.println("알림 실험");
                System.out.println(notification.getContent());
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

    @Override
    public PageResponseDto<NotificationResponse> getNotificationPageList(NotificationListRequestDto requestDto, Pageable pageable, Principal principal) {

        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();

        PageHelper.startPage(pageNum, pageable.getPageSize());
        
        NotificationSearchCondition notificationSearchCondition = requestDto.of(Long.parseLong(principal.getName()));

        List<NotificationResponse> notificationList = mapper.getNotificationList(notificationSearchCondition);

        PageInfo<NotificationResponse> pageInfo = new PageInfo<>(notificationList);


        return new PageResponseDto<>(pageInfo);
    }

    @Transactional
    @Override
    public List<NotificationTargetSimpleResponse> updateTargetListIsRead(List<Long> notificationTargetIds, Long memberId) {

        // 본인의 알림 타겟에서 해당 타겟 id가 있는지 검증하기
        if (mapper.getNotificationTargetListCount(notificationTargetIds,memberId)!=notificationTargetIds.size()) {
            throw new ApplicationException(ErrorCode.NOTIFICATION_CANT_UPDATE);
        }
        mapper.updateTargetListIsRead(notificationTargetIds);

        List<NotificationTargetSimpleResponse> list = notificationTargetIds.stream().map((targetId) ->
             new NotificationTargetSimpleResponse(targetId)
        ).collect(Collectors.toList());
        return list;
    }

    @Transactional
    @Override
    public List<NotificationTargetSimpleResponse> deleteTargetList(List<Long> notificationTargetIds, Long memberId) {

        // 본인의 알림 타겟에서 해당 타겟 id가 있는지 검증하기
        if (mapper.getNotificationTargetListCount(notificationTargetIds,memberId)!=notificationTargetIds.size()) {
            throw new ApplicationException(ErrorCode.NOTIFICATION_CANT_DELETE);
        }

        mapper.deleteTargetList(notificationTargetIds);

        List<NotificationTargetSimpleResponse> list = notificationTargetIds.stream().map((targetId) ->
                new NotificationTargetSimpleResponse(targetId)
        ).collect(Collectors.toList());
        return list;
    }





}
