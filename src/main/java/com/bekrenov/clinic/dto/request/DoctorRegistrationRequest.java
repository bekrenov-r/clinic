package com.bekrenov.clinic.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.PESEL;

public record DoctorRegistrationRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull @Pattern(regexp = "^\\d{9}$", message = "must consist of 9 digits")
        String phoneNumber,
        @NotNull @Email
        String email,
        @NotNull @PESEL
        String pesel,
        @NotBlank @Size(min = 8, max = 50)
        String password,
        @NotBlank
        String occupation,
        @NotNull
        Long departmentId,
        @NotNull @Valid
        AddressRequest address
) implements RegistrationRequest { }
