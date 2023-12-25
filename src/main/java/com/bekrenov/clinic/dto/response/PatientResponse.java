package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.entity.Address;
import com.bekrenov.clinic.model.entity.Patient;

public record PatientResponse(
        String firstName,
        String lastName,
        String pesel,
        String phoneNumber,
        String email,
        Patient.Gender gender,
        Address address
) { }
