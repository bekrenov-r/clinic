package com.bekrenov.clinic.dto.response;

public record DoctorDetailedResponse(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String pesel,
        String occupation,
        DepartmentResponse department
) implements PersonDto { }
