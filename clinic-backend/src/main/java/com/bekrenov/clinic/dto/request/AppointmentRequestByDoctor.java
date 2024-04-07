package com.bekrenov.clinic.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestByDoctor(
        LocalDate date,
        LocalTime time,
        @NotNull
        Long patientId
) implements AppointmentRequest { }
