package com.bekrenov.clinic.dto.response;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        String occupation
) { }
