package com.douzone.prosync.notification.controller;

import com.douzone.prosync.notification.dto.request.NotificationListRequestDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.dto.response.NotificationTargetSimpleResponse;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.notification.service.WebNotificationServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "notification", description = "알림 API")
public class NotificationController {

    private final WebNotificationServiceImpl notificationService;
    private final NotificationMapper mapper;


    /**
     * 구독하기 (+ 안읽은 메시지 갯수 알림 보내주기)
     */
    // TODO: 토큰값을 이용하여 memberId를 사용하도록 수정
    @GetMapping(value ="/subscribe/{id}", produces = "text/event-stream")
    @Operation(summary = "SSE 연결 구독하기", description = "클라이언트와 서버 간의 SSE 연결을 하고 연결 성공 시 사용자가 안읽은 알림에 대한 갯수를 보내줍니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = SseEmitter.class),
            @ApiResponse(code = 503, message = "server connection error")
    })
    @Transactional
    public SseEmitter subscribe(Principal principal){
        return notificationService.subscribe(Long.parseLong(principal.getName()));
    }


    /**
     * 알림 검색 조건 및 페이지 관련 정보에 따른 페이지네이션 처리 로직
     * @param requestDto 알림 검색 조건 및 페이지 관련 정보
     * @return
     */
    @GetMapping("/notificationList")
    @Operation(summary = "알림 리스트 페이지네이션", description = "알림 조건에 따라 알림 리스트 페이지네이션을 조회해서 보내줍니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = PageInfo.class),
            @ApiResponse(code = 500, message = "server error")
    })
    @Transactional(readOnly = true)
    public ResponseEntity<PageInfo<NotificationResponse>> notificationPageList(@RequestBody  NotificationListRequestDto requestDto,@Parameter(hidden = true) Principal principal){

        if(requestDto.getPageNum() == null){
            requestDto.setPageNum(DEFAULT_PAGE_NUM);
            if(requestDto.getPageSize() == null){
                requestDto.setPageSize(DEFAULT_PAGE_SIZE);
            }
        }

        PageHelper.startPage(requestDto.getPageNum(), requestDto.getPageSize());
        return new ResponseEntity<>(new PageInfo<>(mapper.getNotificationList(requestDto.of(Long.parseLong(principal.getName()))), PAGE_NAVI), HttpStatus.OK);
    }

    /**
     * 알림 읽음 처리 로직
     * @param id 읽음 처리할 NotificationTarget pk
     * @return
     */
    // 알림 읽음 처리 로직
    @PatchMapping("/notification/{id}/read")
    @Operation(summary = "알림 읽음 처리", description = "사용자가 해당 알림을 클릭할 시 알림을 읽음으로 업데이트합니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = NotificationTargetSimpleResponse.class),
            @ApiResponse(code = 403, message = "notification cant read"),
            @ApiResponse(code = 404, message = "notification not found")
    })
    public ResponseEntity<NotificationTargetSimpleResponse> updateNotificationIsRead(@Parameter(name = "notificationTargetId", description = "알림 타겟 식별자", required = true, in = ParameterIn.PATH) @PathVariable("id") Long id, @Parameter(hidden = true) Principal principal) {
        NotificationTargetSimpleResponse response = notificationService.updateNotificationIsRead(id, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
