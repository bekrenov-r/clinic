package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        String prescription,
        String details,
        DepartmentResponse department,
        PatientResponse patient,
        PersonDTO doctor
) { }
