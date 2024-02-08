package com.bekrenov.clinic.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequestByPatient(
        LocalDate date,
        LocalTime time,
        Long departmentId,
        Long doctorId,
        boolean anyDoctor
) { }
