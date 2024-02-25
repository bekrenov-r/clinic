package com.bekrenov.clinic.dto.request.validation;

import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DoctorIdValidator implements ConstraintValidator<DoctorIdConstraint, AppointmentRequestByPatient> {
    @Override
    public boolean isValid(AppointmentRequestByPatient request, ConstraintValidatorContext context) {
        System.out.println(request.anyDoctor());
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("doctorId")
                .addConstraintViolation();
        return request.anyDoctor() || request.doctorId() != null;
    }
}
