package com.bekrenov.clinic.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestByDoctor(
        LocalDate date,
        LocalTime time,
        Long patientId,
        @Valid
        PatientRequest patient
) implements AppointmentRequest { }
