package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.DoctorRegistrationRequest;
import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.DoctorShortResponse;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.repository.DepartmentRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, AddressMapper.class})
public abstract class DoctorMapper {
    @Autowired
    protected DepartmentRepository departmentRepository;

    public abstract DoctorShortResponse entityToShortResponse(Doctor doctor);

    public abstract DoctorDetailedResponse entityToDetailedResponse(Doctor doctor);

    @Mapping(source = "departmentId", target = "department", qualifiedByName = "mapDepartment")
    public abstract Doctor requestToEntity(DoctorRegistrationRequest request);

    @Named("mapDepartment")
    protected Department mapDepartment(Long departmentId){
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, departmentId));
    }
}