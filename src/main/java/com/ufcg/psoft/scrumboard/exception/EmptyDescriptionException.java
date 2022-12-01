package com.ufcg.psoft.scrumboard.exception;

public class EmptyDescriptionException extends RuntimeException {

    public EmptyDescriptionException(String message) {
        super(message);
    }

    public EmptyDescriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
