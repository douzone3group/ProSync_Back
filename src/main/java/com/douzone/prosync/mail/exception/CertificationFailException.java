package com.douzone.prosync.mail.exception;

public class CertificationFailException extends RuntimeException{
    public CertificationFailException() {
        super();
    }

    public CertificationFailException(String message) {
        super(message);
    }

    public CertificationFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificationFailException(Throwable cause) {
        super(cause);
    }

    protected CertificationFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}