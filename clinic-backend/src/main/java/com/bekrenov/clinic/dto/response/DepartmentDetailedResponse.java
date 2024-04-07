package com.bekrenov.clinic.dto.response;

import com.bekrenov.clinic.model.entity.Address;

import java.util.List;

public record DepartmentDetailedResponse(
        Long id,
        String name,
        String specialization,
        Boolean autoConfirmAppointment,
        Address address,
        List<EmployeeResponse> employees
) { }
