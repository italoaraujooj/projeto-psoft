package com.ufcg.psoft.scrumboard.exception;

public class UserStoryNotFoundException extends RuntimeException {

    public UserStoryNotFoundException(String message) {
        super(message);
    }

    public UserStoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
