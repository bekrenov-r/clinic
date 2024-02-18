package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentShortResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        String departmentAddress,
        PersonDTO patient,
        PersonDTO doctor
) { }
