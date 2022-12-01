package com.ufcg.psoft.scrumboard.exception;

public class UserCanNotBeProjectOwner extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserCanNotBeProjectOwner(String msg) {
        super(msg);
    }

    public UserCanNotBeProjectOwner(String message, Throwable cause) {
        super(message, cause);
    }
}

