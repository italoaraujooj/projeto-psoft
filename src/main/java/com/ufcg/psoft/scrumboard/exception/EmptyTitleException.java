package com.ufcg.psoft.scrumboard.exception;

public class EmptyTitleException extends RuntimeException {

    public EmptyTitleException(String message) {
        super(message);
    }

    public EmptyTitleException(String message, Throwable cause) {
        super(message, cause);
    }
}
