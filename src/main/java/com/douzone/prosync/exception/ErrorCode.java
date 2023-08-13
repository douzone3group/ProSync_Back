package com.douzone.prosync.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // COMMON
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),
    ACCESS_FORBIDDEN(HttpStatus.FORBIDDEN, "Access forbidden"),
    STATUS_VALUE_NOT_FOUND(HttpStatus.NOT_FOUND, "status value not founded"),

    // MEMBER
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "User ID is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"User Authentication is failed"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    INVALID_REFRESH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "RefreshToken is invalid"),

    // PROJECT
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not founded"),
    PROJECT_EXISTS(HttpStatus.CONFLICT, "Project exists"),

    // TASK
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "Task not founded"),
    TASK_EXISTS(HttpStatus.CONFLICT, "Task exists"),;
    // NOTIFICATION


    // LOG


    // COMMENTS


    private HttpStatus status;
    private String message;
}