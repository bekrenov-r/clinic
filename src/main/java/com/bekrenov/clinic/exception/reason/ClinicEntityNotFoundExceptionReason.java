package com.bekrenov.clinic.exception.reason;

import com.bekrenov.clinic.exception.policy.ExceptionReasonPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ClinicEntityNotFoundExceptionReason implements ExceptionReasonPolicy {
    ACTIVATION_TOKEN("Activation token [%s] not found");

    ClinicEntityNotFoundExceptionReason(String message){
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }

    private final String message;
    private final HttpStatus status;
}
