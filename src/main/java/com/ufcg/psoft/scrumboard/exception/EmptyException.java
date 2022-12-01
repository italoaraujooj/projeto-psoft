package com.ufcg.psoft.scrumboard.exception;

public class EmptyException extends RuntimeException {

    public EmptyException(String message) {
        super(message);
    }

    public EmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
