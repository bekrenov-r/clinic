package com.bekrenov.clinic.dto.request;

import com.bekrenov.clinic.model.entity.Department;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        @NotBlank
        String departmentName,
        @NotNull
        Department.Specialization specialization,
        @Valid
        AddressRequest address
) { }
