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
    NOT_ENTITY_OWNER("You are not owner of this entity", HttpStatus.FORBIDDEN),
    CANNOT_DELETE_DEPARTMENT_WITH_DOCTORS("Department with id [%s] still has associated doctors and therefore cannot be deleted", HttpStatus.CONFLICT),
    APPOINTMENT_TIME_IS_NOT_AVAILABLE("Appointment time [%s] is not available on date [%s]", HttpStatus.BAD_REQUEST),
    NO_AVAILABLE_DOCTORS_IN_DEPARTMENT("Selected department has no doctors available on given date", HttpStatus.BAD_REQUEST),
    DOCTOR_IS_NOT_FROM_DEPARTMENT("Doctor with id [%s] is not registered in given department", HttpStatus.BAD_REQUEST),
    PATIENT_ALREADY_HAS_APPOINTMENT_AT_DATETIME("This patient already has an appointment on date [%s] at [%s]", HttpStatus.CONFLICT),
    DEPARTMENT_WITH_NAME_ALREADY_EXISTS("Department with name [%s] already exists", HttpStatus.CONFLICT),
    DEPARTMENT_WITH_ADDRESS_ALREADY_EXISTS("Department at this address already exists", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;
}
