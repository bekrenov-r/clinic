package com.bekrenov.clinic.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.PESEL;

// todo: add validation
public record PatientRegistrationRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull @PESEL
        String pesel,
        @NotNull @Email
        String email,
        @NotNull @Pattern(regexp = "^\\d{9}$", message = "must consist of 9 digits")
        String phoneNumber,
        @NotBlank @Size(min = 8, max = 50)
        String password,
        @NotNull @Valid
        AddressRequest address
) implements RegistrationRequest { }
