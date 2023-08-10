package com.douzone.prosync.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {

    NO_STATUS,
    TODO,
    IN_PROGRESS,
    DONE;


}
