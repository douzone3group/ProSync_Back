package com.douzone.prosync.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSimpleDto {

    private Long taskId;

    private String title;

    private Long proejctId;
}
