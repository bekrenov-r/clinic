package com.bekrenov.clinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClinicApplicationException extends RuntimeException {
    private ClinicApplicationExceptionReason reason;
    private String message;
    private HttpStatus status;

    public ClinicApplicationException(ClinicApplicationExceptionReason reason) {
        this.message = reason.getMessage();
        this.status = reason.getStatus();
    }

    public ClinicApplicationException(ClinicApplicationExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
        this.status = reason.getStatus();
    }
}
