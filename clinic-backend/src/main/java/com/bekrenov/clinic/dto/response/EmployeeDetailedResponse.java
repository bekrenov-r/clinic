package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.entity.Address;

public record EmployeeDetailedResponse(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String pesel,
        String occupation,
        Address address,
        DepartmentResponse department
) { }
