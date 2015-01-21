package me.reckter.entity.logic;

/**
 * Created by hannes on 21/01/15.
 */
public class LogicReflectionException extends RuntimeException {
    public LogicReflectionException() {
    }

    public LogicReflectionException(String message) {
        super(message);
    }

    public LogicReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicReflectionException(Throwable cause) {
        super(cause);
    }

    public LogicReflectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
