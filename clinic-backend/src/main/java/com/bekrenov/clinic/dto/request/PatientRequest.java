package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.validation.constraint.group.PatientRegistrationWithUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.pl.PESEL;

public record PatientRequest(
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
        @NotBlank(groups = PatientRegistrationWithUser.class)
        @Size(min = 8, max = 50, groups = PatientRegistrationWithUser.class)
        String password,
        @NotNull @Valid
        AddressRequest address
) implements RegistrationRequest { }
