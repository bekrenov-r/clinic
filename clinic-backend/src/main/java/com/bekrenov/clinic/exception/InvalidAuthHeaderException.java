package com.bekrenov.clinic.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAuthHeaderException extends AuthenticationException {
    private static final String DEFAULT_MESSAGE = "Authorization header is invalid or is not present";

    public InvalidAuthHeaderException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidAuthHeaderException(String msg) {
        super(msg);
    }

    public InvalidAuthHeaderException() {
        super(DEFAULT_MESSAGE);
    }
}
