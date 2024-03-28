package com.bekrenov.clinic.validation.annotation;

import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoctorIdValidator implements ConstraintValidator<DoctorIdConstraint, AppointmentRequestByPatient> {
    @Override
    public boolean isValid(AppointmentRequestByPatient request, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("doctorId")
                .addConstraintViolation();
        return request.anyDoctor() || request.doctorId() != null;
    }
}
