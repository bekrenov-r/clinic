package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.entity.Address;

public record DepartmentResponse(
        Long id,
        String name,
        String specialization,
        Address address
) { }
