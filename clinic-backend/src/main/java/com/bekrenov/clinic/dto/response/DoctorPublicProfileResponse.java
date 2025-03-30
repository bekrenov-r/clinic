package com.bekrenov.clinic.dto.response;

public record DoctorPublicProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String occupation
) { }
