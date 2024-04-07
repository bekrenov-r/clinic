package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.model.entity.Department;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        @NotBlank
        String name,
        @NotNull
        Department.Specialization specialization,
        @NotNull
        Boolean autoConfirmAppointment,
        @Valid
        AddressRequest address
) { }
