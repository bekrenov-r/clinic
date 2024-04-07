package com.bekrenov.clinic.validation;

import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.DOCTOR_IS_NOT_FROM_DEPARTMENT;

public class DoctorAssert {
    public static void assertDoctorIsFromDepartment(Doctor doctor, Department department) throws ClinicApplicationException {
        if(!doctor.getDepartment().equals(department))
            throw new ClinicApplicationException(DOCTOR_IS_NOT_FROM_DEPARTMENT, doctor.getId());
    }
}
