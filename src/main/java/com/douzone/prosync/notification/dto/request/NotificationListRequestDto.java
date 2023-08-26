package com.douzone.prosync.notification.dto.request;

import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationListRequestDto {

    private Long memberId;
    private NotificationCode notiCode;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private Integer pageNum;
    private Integer pageSize;

    public NotificationSearchCondition of(){
        return NotificationSearchCondition.builder()
                .memberId(memberId)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .notiCode(notiCode.getCode())
                .build();
    }
}
