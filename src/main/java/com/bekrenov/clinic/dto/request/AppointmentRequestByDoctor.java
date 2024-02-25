package com.bekrenov.clinic.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestByDoctor(
        @FutureOrPresent
        @NotNull
        LocalDate date,
        @NotNull
        LocalTime time,
        @NotNull
        Long patientId
) { }
