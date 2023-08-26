package com.douzone.prosync.notification.controller;

import com.douzone.prosync.notification.dto.request.NotificationListRequestDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.dto.response.NotificationTargetSimpleResponse;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.notification.service.WebNotificationServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationController {

    private final WebNotificationServiceImpl notificationService;
    private final NotificationMapper mapper;



    /**
     * 구독하기 (+ 안읽은 메시지 갯수 알림 보내주기)
     */
    // TODO: 토큰값을 이용하여 memberId를 사용하도록 수정
    @GetMapping(value ="/subscribe/{id}", produces = "text/event-stream")
    @Transactional
    public SseEmitter subscribe(@PathVariable("id") Long memberId){
        return notificationService.subscribe(memberId);
    }

    @GetMapping("/notificationList")
    @Transactional(readOnly = true)
    public ResponseEntity<PageInfo<NotificationResponse>> notificationPageList(@RequestBody NotificationListRequestDto requestDto){

        if(requestDto.getPageNum() == null){
            requestDto.setPageNum(DEFAULT_PAGE_NUM);
            if(requestDto.getPageSize() == null){
                requestDto.setPageSize(DEFAULT_PAGE_SIZE);
            }
        }

        PageHelper.startPage(requestDto.getPageNum(), requestDto.getPageSize());
        return new ResponseEntity<>(new PageInfo<>(mapper.getNotificationList(requestDto.of()), PAGE_NAVI), HttpStatus.OK);
    }

    // 알림 읽음 처리 로직
    @PatchMapping("/notification/{id}/read")
    public ResponseEntity<NotificationTargetSimpleResponse> updateNotificationIsRead(@PathVariable("id") Long id,Principal principal) {
        NotificationTargetSimpleResponse response = notificationService.updateNotificationIsRead(id, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
