package com.ufcg.psoft.scrumboard.exception;

public class TitleAlreadyExistException extends RuntimeException {

    public TitleAlreadyExistException(String message) {
        super(message);
    }

    public TitleAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
