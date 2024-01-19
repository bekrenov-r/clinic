package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.DoctorShortResponse;
import com.bekrenov.clinic.model.entity.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DepartmentMapper.class)
public abstract class DoctorMapper {
    public abstract DoctorShortResponse entityToShortResponse(Doctor doctor);

    public abstract DoctorDetailedResponse entityToDetailedResponse(Doctor doctor);
}
