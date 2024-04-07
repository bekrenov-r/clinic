package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.response.EmployeeResponse;
import com.bekrenov.clinic.model.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {
    public abstract EmployeeResponse employeeToResponse(Employee employee);
}
