package com.ufcg.psoft.scrumboard.exception;


public class UserNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String msg) {
        super(msg);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
