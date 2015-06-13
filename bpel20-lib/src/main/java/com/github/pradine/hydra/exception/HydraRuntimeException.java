package com.github.pradine.hydra.exception;

public class HydraRuntimeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1968744262384889002L;

    public HydraRuntimeException() {
        super();
    }

    public HydraRuntimeException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HydraRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HydraRuntimeException(String message) {
        super(message);
    }

    public HydraRuntimeException(Throwable cause) {
        super(cause);
    }

}
