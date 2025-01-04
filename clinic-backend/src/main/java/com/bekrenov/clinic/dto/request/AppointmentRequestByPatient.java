package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.validation.constraint.DoctorIdConstraint;
import com.bekrenov.clinic.validation.constraint.PatientRequestConstraint;
import com.bekrenov.clinic.validation.constraint.group.PatientRegistrationWithUser;
import com.bekrenov.clinic.validation.constraint.group.PatientRegistrationWithoutUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;

@DoctorIdConstraint(groups = {PatientRegistrationWithUser.class, PatientRegistrationWithoutUser.class})
public record AppointmentRequestByPatient(
        LocalDate date,
        LocalTime time,
        @NotNull
        Long departmentId,
        Long doctorId,
        boolean anyDoctor,
        PatientRequest patient
) implements AppointmentRequest { }
