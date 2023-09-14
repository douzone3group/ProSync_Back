package com.douzone.prosync.notification.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NotificationTargetIdsDto {

    private List<Long> notificationTargetIds;
    private String key;
}
