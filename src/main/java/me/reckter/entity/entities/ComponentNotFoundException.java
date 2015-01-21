package me.reckter.entity.entities;

/**
 * Created by hannes on 21.01.15.
 */
public class ComponentNotFoundException extends RuntimeException {
    public ComponentNotFoundException() {
    }

    public ComponentNotFoundException(String message) {
        super(message);
    }

    public ComponentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentNotFoundException(Throwable cause) {
        super(cause);
    }

    public ComponentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
