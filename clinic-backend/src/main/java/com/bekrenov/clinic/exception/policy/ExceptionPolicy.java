package com.bekrenov.clinic.exception.policy;

import org.springframework.http.HttpStatus;

public interface ExceptionPolicy {
    ExceptionReasonPolicy getReason();
    String getMessage();
    HttpStatus getStatus();
}
