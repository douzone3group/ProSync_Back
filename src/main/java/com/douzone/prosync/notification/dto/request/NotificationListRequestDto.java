package com.douzone.prosync.notification.dto.request;

import com.douzone.prosync.notification.notienum.NotificationCode;
import com.douzone.prosync.searchcondition.NotificationSearchCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;

@Schema(description = "알림 리스트 요청 DTO")
@Data
@AllArgsConstructor
public class NotificationListRequestDto {

    @Schema(description = "알림 코드", example = "업무제외")
    private NotificationCode notiCode;

    @Schema(description = "검색 시작 날짜", example = "2023/08/26 00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "검색 마감 날짜", example = "2023/08/26 00:00:00")
    private LocalDateTime endDate;

    @Schema(description = "단어 검색 조건", example = "업무")
    private String content;


    public NotificationSearchCondition of(Long memberId){
        if (notiCode==null) {
            return NotificationSearchCondition.builder()
                    .memberId(memberId)
                    .content(content)
                    .startDate(startDate)
                    .endDate(endDate)
                    .notiCode(null)
                    .build();
        } else {
            return NotificationSearchCondition.builder()
                    .memberId(memberId)
                    .content(content)
                    .startDate(startDate)
                    .endDate(endDate)
                    .notiCode(notiCode.name())
                    .build();
        }

    }
}
