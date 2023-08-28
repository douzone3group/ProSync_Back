package com.douzone.prosync.notification.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "알림 타겟 응답 DTO")
@Getter
@AllArgsConstructor
public class NotificationTargetSimpleResponse {

        @Schema(description = "알림 타겟 식별자", example = "1")
        private Long notificationTargetId;
}
