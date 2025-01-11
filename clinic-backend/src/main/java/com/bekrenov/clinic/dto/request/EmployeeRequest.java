package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.model.enums.Occupation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.pl.PESEL;

public record EmployeeRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String email,
        @NotBlank @PESEL
        String pesel,
        @NotBlank
        Occupation occupation,
        @NotNull @Valid
        AddressRequest address,
        Long departmentId
) { }
