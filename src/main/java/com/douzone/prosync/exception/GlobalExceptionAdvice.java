package com.douzone.prosync.exception;

import com.douzone.prosync.mail.exception.CertificationFailException;
import com.douzone.prosync.security.exception.DuplicateMemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {


    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> applicationHandler(ApplicationException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode().name()));
    }

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<?> applicationHandler(DuplicateMemberException e) {
        log.error("Error occurs {}", e.toString());
        // Conflict : 409번 요청과 현재 서버의 리소스 상태가 충돌됐다는 것을 의미한다.
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(ErrorCode.DUPLICATED_USER_ID.name()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> applicationHandler(AuthenticationException e) {
        log.error("Error occurs {}", e.toString());
        // Unauthorized : 401번 인증 실패
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ErrorCode.UNAUTHORIZED.name()));
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }

    // 메일 인증 오류

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<?> handleEmailFailure(MailSendException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.INVALID_EMAIL.name()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> handleEmailFailure(MessagingException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.EMAIL_NOT_SENT.name()));
    }

    // 인증번호 오류

    @ExceptionHandler(CertificationFailException.class)
    public ResponseEntity<?> certificationFail(CertificationFailException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.CERTIFICATION_NUMBER_MISMATCH.name()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        return ErrorResponse.of(e.getBindingResult());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException", e);
        return ErrorResponse.of(e.getConstraintViolations());
    }

}