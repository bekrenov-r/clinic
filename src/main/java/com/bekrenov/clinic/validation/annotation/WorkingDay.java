package com.bekrenov.clinic.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WorkingDayValidator.class)
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkingDay {
    String message() default "must be working day";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
