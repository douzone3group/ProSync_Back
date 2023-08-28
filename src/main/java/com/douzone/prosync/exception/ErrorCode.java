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

    // TASK
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "Task not founded"),
    TASK_EXISTS(HttpStatus.CONFLICT, "Task exists"),

    // TASK_STATUS
    TASK_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "Task Status not founded"),

    // FILE
    INVALID_FILE_TYPE(HttpStatus.FORBIDDEN, "Invalid file type"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File not founded"),;

    // NOTIFICATION


    // LOG


    // COMMENTS


    private HttpStatus status;
    private String message;
}

