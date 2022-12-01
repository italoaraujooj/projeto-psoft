package com.ufcg.psoft.scrumboard.exception;

public class ProjectNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ProjectNotFoundException(String msg) {
        super(msg);
    }

    public ProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
