package com.bekrenov.clinic.validation.constraint;

import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DoctorIdValidator implements ConstraintValidator<DoctorIdConstraint, AppointmentRequestByPatient> {
    @Override
    public boolean isValid(AppointmentRequestByPatient request, ConstraintValidatorContext context) {
        log.info("Validating {}", request);
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("doctorId")
                .addConstraintViolation();
        return request.anyDoctor() || request.doctorId() != null;
    }
}
