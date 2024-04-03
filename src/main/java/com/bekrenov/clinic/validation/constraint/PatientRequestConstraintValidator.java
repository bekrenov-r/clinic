package com.bekrenov.clinic.validation.constraint;

import com.bekrenov.clinic.dto.request.PatientRequest;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PatientRequestConstraintValidator implements ConstraintValidator<PatientRequestConstraint, PatientRequest> {
    @Override
    public boolean isValid(PatientRequest patientRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(!CurrentAuthUtil.isAuthenticated() && patientRequest == null)
            return false;
        return true;
    }
}
