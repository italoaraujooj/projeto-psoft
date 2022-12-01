package com.ufcg.psoft.scrumboard.exception;

public class EmptyRepositoryException extends RuntimeException {

    public EmptyRepositoryException(String message) {
        super(message);
    }

    public EmptyRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
