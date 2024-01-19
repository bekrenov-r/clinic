package com.bekrenov.clinic.exception.reason;

import com.bekrenov.clinic.exception.policy.ExceptionReasonPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ClinicApplicationExceptionReason implements ExceptionReasonPolicy {
    ALREADY_REGISTERED_EMAIL("User with email [%s] is already registered", HttpStatus.CONFLICT),
    ALREADY_REGISTERED_PHONE_NUMBER("User with phone number [%s] is already registered", HttpStatus.CONFLICT),
    ALREADY_REGISTERED_PESEL("User with pesel [%s] is already registered", HttpStatus.CONFLICT),
    NOT_ENTITY_OWNER("You are not owner of this entity", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;
}
