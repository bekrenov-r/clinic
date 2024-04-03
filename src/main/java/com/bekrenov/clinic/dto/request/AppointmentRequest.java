package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.validation.constraint.WorkingDay;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentRequest {
    @FutureOrPresent
    @WorkingDay
    @NotNull
    LocalDate date();
    @NotNull
    LocalTime time();
}
