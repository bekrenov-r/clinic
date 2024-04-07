package com.bekrenov.clinic.validation;

import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.model.entity.Address;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.DEPARTMENT_WITH_ADDRESS_ALREADY_EXISTS;
import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.DEPARTMENT_WITH_NAME_ALREADY_EXISTS;

@Component
@RequiredArgsConstructor
public class DepartmentValidator {
    private final DepartmentRepository departmentRepository;

    public void validateDepartment(Department department){
        assertNameIsUnique(department);
        assertAddressIsUnique(department);
    }

    private void assertNameIsUnique(Department department) {
        if(departmentRepository.existsByName(department.getName()))
            throw new ClinicApplicationException(DEPARTMENT_WITH_NAME_ALREADY_EXISTS, department.getName());
    }

    private void assertAddressIsUnique(Department department){
        Address address = department.getAddress();
        boolean isUniqueByAddress = departmentRepository.findAll().stream()
                .map(Department::getAddress)
                .noneMatch(address::equals);
        if(!isUniqueByAddress)
            throw new ClinicApplicationException(DEPARTMENT_WITH_ADDRESS_ALREADY_EXISTS);
    }
}
