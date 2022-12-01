package com.ufcg.psoft.scrumboard.exception;

public class UserNotBeScrumMaster extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserNotBeScrumMaster(String msg) {
        super(msg);
    }

    public UserNotBeScrumMaster(String message, Throwable cause) {
        super(message, cause);
    }
}
