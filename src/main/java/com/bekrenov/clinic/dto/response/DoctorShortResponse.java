package com.bekrenov.clinic.dto.response;

public record DoctorShortResponse(
        Long id,
        String firstName,
        String lastName
) implements PersonDto { }
