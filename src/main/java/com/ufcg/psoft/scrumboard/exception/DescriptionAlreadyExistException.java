package com.ufcg.psoft.scrumboard.exception;

public class DescriptionAlreadyExistException extends RuntimeException {

    public DescriptionAlreadyExistException(String message) {
        super(message);
    }

    public DescriptionAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
