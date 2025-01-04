package com.bekrenov.clinic.validation.constraint;

import com.bekrenov.clinic.dto.request.PatientRequest;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PatientRequestConstraintValidator implements ConstraintValidator<PatientRequestConstraint, PatientRequest> {
    @Override
    public boolean isValid(PatientRequest patientRequest, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating {}", patientRequest);
        if(!CurrentAuthUtil.isAuthenticated() && patientRequest == null)
            return false;
        return true;
    }
}
