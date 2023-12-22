package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.entity.Address;

public record PatientResponse(
        String firstName,
        String lastName,
        String pesel,
        String phoneNumber,
        String email,
        Address address
) { }
