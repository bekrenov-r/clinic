package com.bekrenov.clinic.exception.policy;

import org.springframework.http.HttpStatus;

public interface ExceptionReasonPolicy {
    String getMessage();
    HttpStatus getStatus();
}
