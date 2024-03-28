package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.validation.annotation.DoctorIdConstraint;
import com.bekrenov.clinic.validation.annotation.WorkingDay;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@DoctorIdConstraint
public record AppointmentRequestByPatient(
        @FutureOrPresent
        @WorkingDay
        @NotNull
        LocalDate date,
        @NotNull
        LocalTime time,
        @NotNull
        Long departmentId,
        Long doctorId,
        boolean anyDoctor
) { }
