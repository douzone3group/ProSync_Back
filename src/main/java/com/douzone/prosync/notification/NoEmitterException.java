package com.douzone.prosync.notification;

public class NoEmitterException extends RuntimeException{
    public NoEmitterException() {
        super();
    }

    public NoEmitterException(String message) {
        super(message);
    }

    public NoEmitterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEmitterException(Throwable cause) {
        super(cause);
    }

    protected NoEmitterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
