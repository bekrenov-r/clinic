package com.bekrenov.clinic.exception;

import com.bekrenov.clinic.exception.policy.ExceptionPolicy;
import com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClinicEntityNotFoundException extends RuntimeException implements ExceptionPolicy {
    private ClinicEntityNotFoundExceptionReason reason;
    private String message;
    private HttpStatus status;

    public ClinicEntityNotFoundException(ClinicEntityNotFoundExceptionReason reason) {
        this.message = reason.getMessage();
        this.status = reason.getStatus();
    }

    public ClinicEntityNotFoundException(ClinicEntityNotFoundExceptionReason reason, Object... args) {
        this.message = String.format(reason.getMessage(), args);
        this.reason = reason;
        this.status = reason.getStatus();
    }
}
