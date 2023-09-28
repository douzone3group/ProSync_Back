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
    INVALID_VALUE(HttpStatus.FORBIDDEN, "Invalid value"),
    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST, "Invalid enum value"),

    // MEMBER
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "User ID is duplicated"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"User Authentication is failed"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "RefreshToken is invalid"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "No recipient adresses"),
    EMAIL_NOT_SENT(HttpStatus.INTERNAL_SERVER_ERROR,"Email could not be sent"),
    CERTIFICATION_NUMBER_MISMATCH(HttpStatus.BAD_REQUEST,"Certification number is mismatching"),

    CRYPT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Crypt error"),

    // PROJECT
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "Project not founded"),
    PROJECT_EXISTS(HttpStatus.CONFLICT, "Project exists"),
    PROJECT_LINK_NOT_FOUND(HttpStatus.NOT_FOUND, "invite link not founded"),

    // PROJECT_MEMBER
    PROJECT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Project Member not founded"),
    PROJECT_MEMBER_EXISTS(HttpStatus.CONFLICT, "Project Member exists"),
    PROJECT_INVITE_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "Project code not founded"),

    // TASK
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "Task not founded"),
    TASK_EXISTS(HttpStatus.CONFLICT, "Task exists"),

    // PROJECT_MEMBER
    TASK_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Task Member not founded"),
    TASK_MEMBER_EXISTS(HttpStatus.CONFLICT, "Task Member exists"),

    // TASK_STATUS
    TASK_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "Task Status not founded"),

    // FILE
    INVALID_FILE_TYPE(HttpStatus.FORBIDDEN, "Invalid file type"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File not founded"),
    FILE_INFO_EXISTS(HttpStatus.CONFLICT, "File Info exists"),

    // NOTIFICATION
    CONNECTION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "Sse Connection is failed"),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Notification not founded"),
    NOTIFICATION_CANT_READ(HttpStatus.FORBIDDEN, "Notification not read"),

    NOTIFICATION_CANT_UPDATE(HttpStatus.BAD_REQUEST, "Notification cant update"),

    NOTIFICATION_CANT_DELETE(HttpStatus.BAD_REQUEST, "Notification cant delete"),


    // LOG


    // COMMENTS


    // AUTHORIZATION
    MEMBER_NOT_INCLUDED_IN_PROJECT(HttpStatus.FORBIDDEN,"Member not included in project"),
    INAPPROPRIATE_PERMISSION(HttpStatus.FORBIDDEN, "Unauthorized User");

    private HttpStatus status;
    private String message;
}

