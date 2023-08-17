package com.douzone.prosync.security.exception;

public class FailToRenewTokenException extends RuntimeException{
    public FailToRenewTokenException() {
        super();
    }

    public FailToRenewTokenException(String message) {
        super(message);
    }

    public FailToRenewTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailToRenewTokenException(Throwable cause) {
        super(cause);
    }

    protected FailToRenewTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
