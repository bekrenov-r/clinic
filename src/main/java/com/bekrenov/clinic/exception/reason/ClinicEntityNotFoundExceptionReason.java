package com.bekrenov.clinic.exception.reason;

import com.bekrenov.clinic.exception.policy.ExceptionReasonPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ClinicEntityNotFoundExceptionReason implements ExceptionReasonPolicy {
    ACTIVATION_TOKEN("Activation token [%s] not found"),
    DEPARTMENT("Department with id [%s] does not exist"),
    SPECIALIZATION("Specialization with id [%s] does not exist"),
    DOCTOR("Doctor with id [%s] does not exist"),
    PATIENT("Patient with id [%s] does not exist"),
    APPOINTMENT("Appointment with id [%s] does not exist");

    ClinicEntityNotFoundExceptionReason(String message){
        this.message = message;
        this.status = HttpStatus.NOT_FOUND;
    }

    private final String message;
    private final HttpStatus status;
}
