package com.ufcg.psoft.scrumboard.exception;

public class UserNotBeProjectOwner extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserNotBeProjectOwner(String msg) {
        super(msg);
    }

    public UserNotBeProjectOwner(String message, Throwable cause) {
        super(message, cause);
    }
}
