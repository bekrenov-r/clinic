package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.DepartmentRequest;
import com.bekrenov.clinic.dto.response.DepartmentResponse;
import com.bekrenov.clinic.model.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class DepartmentMapper {
    public abstract DepartmentResponse entityToResponse(Department department);
    public abstract Department requestToEntity(DepartmentRequest request);
}
