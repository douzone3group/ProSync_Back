package com.douzone.prosync.task_status.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BasicTaskStatus {

    NO_STATUS(1, "No Status", "#e0e1dd"),
    TODO(2, "Todo", "#588157"),
    IN_PROGRESS(3, "In Progress", "#bc6c25"),
    DONE(4, "Done", "#8338ec"),;

    private Integer seq;
    private String taskStatus;
    private String color;

}
