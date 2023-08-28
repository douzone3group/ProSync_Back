package com.douzone.prosync.notification.dto.request;

import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "알림 리스트 요청 DTO")
@Data
public class NotificationListRequestDto {

    @Schema(description = "알림 코드", example = "업무제외")
    private NotificationCode notiCode;

    @Schema(description = "검색 시작 날짜", example = "2023/08/26 00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "검색 마감 날짜", example = "2023/08/26 00:00:00")
    private LocalDateTime endDate;

    @Schema(description = "단어 검색 조건", example = "업무")
    private String content;

    @Schema(description = "현재 페이지", defaultValue = "1", example = "4")
    private Integer pageNum;

    @Schema(description = "한 페이지의 데이터 갯수",defaultValue = "10", example = "20")
    private Integer pageSize;

    public NotificationSearchCondition of(Long memberId){
        return NotificationSearchCondition.builder()
                .memberId(memberId)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .notiCode(notiCode.getCode())
                .build();
    }
}
