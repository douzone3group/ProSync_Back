package com.douzone.prosync.notification.controller;

import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ErrorResponse;
import com.douzone.prosync.notification.dto.request.NotificationListRequestDto;
import com.douzone.prosync.notification.dto.request.NotificationTargetIdsDto;
import com.douzone.prosync.notification.dto.response.NotificationResponse;
import com.douzone.prosync.notification.dto.response.NotificationTargetSimpleResponse;
import com.douzone.prosync.notification.mapper.NotificationMapper;
import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.notification.service.NotificationService;
import com.douzone.prosync.notification.service.WebNotificationServiceImpl;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    @Operation(summary = "SSE 연결 구독하기", description = "클라이언트와 서버 간의 SSE 연결을 하고 연결 성공 시 사용자가 안읽은 알림에 대한 갯수를 보내줍니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = SseEmitter.class),
            @ApiResponse(code = 404, message = "user not founded", response = ErrorResponse.class),
            @ApiResponse(code = 200, message = "sse Connection is failed", response = String.class)
    })
    @Transactional
    public SseEmitter subscribe(@PathVariable("id") Long memberId) {
        return notificationService.subscribe(memberId);
    }


    /**
     * 알림 검색 조건 및 페이지 관련 정보에 따른 페이지네이션 처리 로직
     *
     * @param 알림 검색 조건 및 페이지 관련 정보
     * @return
     */
    @GetMapping("/notificationList")
    @Operation(summary = "알림 리스트 페이지네이션", description = "알림 조건에 따라 알림 리스트 페이지네이션을 조회해서 보내줍니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = PageInfo.class),
            @ApiResponse(code = 500, message = "server error")
    })
    @Transactional(readOnly = true)
    public ResponseEntity<PageResponseDto<NotificationResponse>> notificationPageList(
            @RequestParam(required = false) NotificationCode code,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) String content,
            @Parameter(hidden = true) Principal principal,
            @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {

        NotificationListRequestDto requestDto = new NotificationListRequestDto(code, startDate, endDate, content);
        PageResponseDto<NotificationResponse> notificationPageList = notificationService.getNotificationPageList(requestDto, pageable, principal);

        return new ResponseEntity<>(notificationPageList, HttpStatus.OK);
    }

    /**
     * 알림 읽음 처리 로직
     *
     * @param id 읽음 처리할 NotificationTarget pk
     * @return
     */
    // 알림 읽음 처리 로직
    @PatchMapping("/notification/{id}/read")
    @Operation(summary = "알림 읽음 처리", description = "사용자가 해당 알림을 클릭할 시 알림을 읽음으로 업데이트합니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = NotificationTargetSimpleResponse.class),
            @ApiResponse(code = 403, message = "notification cant read"),
            @ApiResponse(code = 404, message = "notification not found"),
            @ApiResponse(code = 500, message = "server error")
    })
    public ResponseEntity<NotificationTargetSimpleResponse> updateNotificationIsRead(@Parameter(name = "notificationTargetId", description = "알림 타겟 식별자", required = true, in = ParameterIn.PATH) @PathVariable("id") Long id, @Parameter(hidden = true) Principal principal) {
        NotificationTargetSimpleResponse response = notificationService.updateNotificationIsRead(id, Long.parseLong(principal.getName()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 알림 읽음 처리 로직(복수)
     */
    // 알림 읽음 처리 로직
    @PatchMapping("/notification/read")
    @Operation(summary = "알림 읽음 처리", description = "사용자가 선택한 알림들을 읽음으로 업데이트합니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = List.class),
            @ApiResponse(code = 400, message = "notification cant update"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<List<NotificationTargetSimpleResponse>> updateNotificationListIsRead(@Parameter(name = "notificationTargetIds", description = "알림 타겟 식별자(복수)", required = true, in = ParameterIn.DEFAULT)
                                                                                         @RequestBody NotificationTargetIdsDto dto,
                                                                                         @Parameter(hidden = true) Principal principal) {

        List<NotificationTargetSimpleResponse> response= notificationService.updateTargetListIsRead(dto.getNotificationTargetIds(),Long.parseLong(principal.getName()));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * 알림 삭제 처리 로직(복수)
     */
    @DeleteMapping("/notification/delete")
    @Operation(summary = "선택 알림 삭제 처리", description = "사용자가 선택한 알림들을 삭제합니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved", response = List.class),
            @ApiResponse(code = 400, message = "notification cant delete"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity<List<NotificationTargetSimpleResponse>> deleteNotificationList(@Parameter(name = "notificationTargetIds", description = "알림 타겟 식별자(복수)", required = true, in = ParameterIn.DEFAULT)
                                                                                         @RequestBody NotificationTargetIdsDto dto,
                                                                                         HttpServletRequest request,
                                                                                         @Parameter(hidden = true) Principal principal) {

        List<NotificationTargetSimpleResponse> response= notificationService.deleteTargetList(dto.getNotificationTargetIds(),Long.parseLong(principal.getName()));
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @DeleteMapping("/notification/deleteAll")
    @Operation(summary = "모든 알림 삭제 처리", description = "사용자의 모든 알림들을 삭제합니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity deleteAllNotification(@Parameter(hidden = true) Principal principal) {
        mapper.deleteAllTarget(Long.parseLong(principal.getName()));
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 모든 알림 읽기 처리
     */
    // 알림 읽음 처리 로직
    @PatchMapping("/notification/allRead")
    @Operation(summary = "모든 알림 읽음 처리", description = "사용자의 모든 알림을 읽음으로 업데이트합니다.", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "successfully retrieved"),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity updateAllNotificationListIsRead(@Parameter(hidden = true) Principal principal) {

        mapper.updateAllNotificationIsRead(Long.parseLong(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }




    /**
     * 안읽은 알림 갯수 조회
     */
    @GetMapping("/notification/count")
    @Operation(summary = "안읽은 알림 갯수 조회", description = "안읽은 알림 갯수를 조회함", tags = "notification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully retrieved" ,response = Integer.class),
            @ApiResponse(code = 500, message = "server error"),
    })
    public ResponseEntity getNotificationCountIsReadFalse(@Parameter(hidden = true) Principal principal){

        Integer count = mapper.getNotificationCountIsReadFalse(Long.parseLong(principal.getName()));
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


}
