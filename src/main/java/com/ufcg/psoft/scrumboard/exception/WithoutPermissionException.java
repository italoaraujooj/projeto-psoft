package com.ufcg.psoft.scrumboard.exception;

public class WithoutPermissionException extends RuntimeException {

    public WithoutPermissionException(String message) {
        super(message);
    }

    public WithoutPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

}
