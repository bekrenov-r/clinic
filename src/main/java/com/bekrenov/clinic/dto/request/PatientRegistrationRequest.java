package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.model.entity.Patient;

// todo: add validation
public record PatientRegistrationRequest(
        String firstName,
        String lastName,
        String pesel,
        String email,
        String phoneNumber,
        Patient.Gender gender,
        String password,
        AddressRequest address
) implements RegistrationRequest { }
