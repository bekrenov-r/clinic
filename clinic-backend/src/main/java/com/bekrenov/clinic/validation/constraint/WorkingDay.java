package com.bekrenov.clinic.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WorkingDayValidator.class)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WorkingDay {
    String message() default "must be working day";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
