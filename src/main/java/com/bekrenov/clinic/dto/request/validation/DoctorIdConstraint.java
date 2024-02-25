package com.bekrenov.clinic.dto.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DoctorIdValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoctorIdConstraint {
    String message() default "must be present if 'anyDoctor' is 'false'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
