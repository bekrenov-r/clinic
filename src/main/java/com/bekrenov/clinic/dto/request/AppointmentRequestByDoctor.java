package com.bekrenov.clinic.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestByDoctor(
        LocalDate date,
        LocalTime time,
        Long patientId
) { }
