package com.bekrenov.clinic.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PatientRequestConstraintValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PatientRequestConstraint {
    String message() default "must be present if patient is not registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
