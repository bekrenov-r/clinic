package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DepartmentMapper;
import com.bekrenov.clinic.dto.request.DepartmentRequest;
import com.bekrenov.clinic.dto.response.DepartmentDetailedResponse;
import com.bekrenov.clinic.dto.response.DepartmentResponse;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.validation.DepartmentValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.CANNOT_DELETE_DEPARTMENT_WITH_EMPLOYEES;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final DepartmentValidator departmentValidator;

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::entityToResponse)
                .toList();
    }

    public List<DepartmentResponse> getAllDepartmentsBySpecialization(Department.Specialization specialization){
        return departmentRepository.findAllBySpecialization(specialization).stream()
                .map(departmentMapper::entityToResponse)
                .toList();
    }

    public DepartmentDetailedResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
        return departmentMapper.entityToDetailedResponse(department);
    }

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        Department department = departmentMapper.requestToEntity(request);
        departmentValidator.validateDepartment(department);
        return departmentMapper.entityToResponse(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
        assertDepartmentHasNoEmployees(department);
        departmentRepository.delete(department);
    }

    private void assertDepartmentHasNoEmployees(Department department) {
        if(!department.getEmployees().isEmpty())
            throw new ClinicApplicationException(CANNOT_DELETE_DEPARTMENT_WITH_EMPLOYEES, department.getId());
    }
}
