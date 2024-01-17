package com.bekrenov.clinic.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Log4j2
public class ClinicResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest webRequest){
        this.logException(ex);
        ErrorResponse response = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(ClinicApplicationException.class)
    public ResponseEntity<ErrorResponse> handleClinicApplicationException(ClinicApplicationException ex, WebRequest webRequest){
        this.logException(ex);
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(ex.getStatus())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex){
        this.logException(ex);
        ErrorResponse response = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex){
        this.logException(ex);
        ErrorResponse response = ErrorResponse
                .builder()
                .message(ex.getMessage())
                .status(HttpStatus.FORBIDDEN)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        StringBuilder message = new StringBuilder("Errors:");
        for(FieldError error : ex.getFieldErrors()){
            message
                    .append(" [")
                    .append(error.getField())
                    .append(" ")
                    .append(error.getDefaultMessage())
                    .append(", rejected value: ")
                    .append(error.getRejectedValue())
                    .append("]");
        }
        ErrorResponse response = ErrorResponse
                .builder()
                .message(message.toString())
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(status)
                .body(response);
    }

    protected void logException(Exception ex){
        log.error(ex.getClass().getSimpleName() + ": ", ex);
    }
}
