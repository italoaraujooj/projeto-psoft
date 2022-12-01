package com.ufcg.psoft.scrumboard.exception;

public class PositionNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public PositionNotFoundException(String msg) {
        super(msg);
    }

    public PositionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
