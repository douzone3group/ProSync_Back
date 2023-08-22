package com.douzone.prosync.notification.controller;

import com.douzone.prosync.notification.dto.request.NotificationListRequestDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.notification.repository.MapEmitterRepository;
import com.douzone.prosync.notification.service.NotificationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper mapper;
    private final MapEmitterRepository mapEmitterRepository;

    @GetMapping(value ="/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable("id") Long memberId){
        //Long memberId = Long.parseLong(principal.getName());
        System.out.println("씨발");
        return notificationService.subscribe(memberId);
    }

    @GetMapping("/test")
    public PageInfo<NotificationResponse> response(@RequestBody NotificationListRequestDto requestDto){

        if(requestDto.getPageNum() == null){
            requestDto.setPageNum(DEFAULT_PAGE_NUM);
            if(requestDto.getPageSize() == null){
                requestDto.setPageSize(DEFAULT_PAGE_SIZE);
            }
        }

        PageHelper.startPage(requestDto.getPageNum(), requestDto.getPageSize());
        return new PageInfo<>(mapper.getNotificationList(requestDto.of()), PAGE_NAVI);
    }

    @GetMapping("/test2")
    public String test1(@RequestParam("memberId") String memberId){
        notificationService.sendToClient(mapEmitterRepository.findById(Long.valueOf(memberId)), new NotiDto("hi","https://www.youtube.com"));
        return "hi";
    }
}
