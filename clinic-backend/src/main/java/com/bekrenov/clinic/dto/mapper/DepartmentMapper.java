package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.DepartmentRequest;
import com.bekrenov.clinic.dto.response.DepartmentDetailedResponse;
import com.bekrenov.clinic.dto.response.DepartmentResponse;
import com.bekrenov.clinic.model.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = EmployeeMapper.class)
public abstract class DepartmentMapper {
    @Mapping(target = "specialization", source = "specialization", qualifiedByName = "mapSpecialization")
    public abstract DepartmentResponse entityToResponse(Department department);
    public abstract DepartmentDetailedResponse entityToDetailedResponse(Department department);
    public abstract Department requestToEntity(DepartmentRequest request);

    @Named("mapSpecialization")
    protected String mapSpecialization(Department.Specialization specialization) {
        return specialization.getLangNameMap().get("en");
    }
}
