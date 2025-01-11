package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.EmployeeMapper;
import com.bekrenov.clinic.dto.request.EmployeeRequest;
import com.bekrenov.clinic.dto.response.EmployeeDetailedResponse;
import com.bekrenov.clinic.dto.response.EmployeeResponse;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Employee;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.EmployeeRepository;
import com.bekrenov.clinic.security.Role;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public List<EmployeeResponse> getAllActiveEmployees() {
        if(CurrentAuthUtil.hasAuthority(Role.HEAD_OF_DEPARTMENT)) {
            Employee headOfDepartment = employeeRepository.findByEmailOrThrowDefault(
                    CurrentAuthUtil.getAuthentication().getName()
            );
            return employeeRepository.findAllActive().stream()
                    .filter(e -> e.isInDepartment(headOfDepartment.getDepartment()))
                    .map(employeeMapper::employeeToResponse)
                    .toList();
        }
        return employeeRepository.findAllActive().stream()
                .map(employeeMapper::employeeToResponse)
                .toList();
    }

    public List<EmployeeResponse> getAllDismissedEmployees() {
        if(CurrentAuthUtil.hasAuthority(Role.HEAD_OF_DEPARTMENT)) {
            Doctor authenticatedDoctor = doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
            return employeeRepository.findAllDismissed().stream()
                    .filter(e -> e.isInDepartment(authenticatedDoctor.getDepartment()))
                    .map(employeeMapper::employeeToResponse)
                    .toList();
        }
        return employeeRepository.findAllDismissed().stream()
                .map(employeeMapper::employeeToResponse)
                .toList();
    }

    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = employeeMapper.requestToEntity(request);
        employee.setDepartment(resolveDepartment(request));
        employee.setDismissed(false);
        return employeeMapper.employeeToResponse(employeeRepository.save(employee));
    }

    public void dismissEmployee(Long id) {
        Employee employee = employeeRepository.findByIdOrThrowDefault(id);
        employee.setDismissed(true);
        employeeRepository.save(employee);
    }

    private Department resolveDepartment(EmployeeRequest request) {
        if(CurrentAuthUtil.hasAuthority(Role.HEAD_OF_DEPARTMENT)) {
            Employee headOfDepartment = employeeRepository.findByEmailOrThrowDefault(
                    CurrentAuthUtil.getAuthentication().getName()
            );
            return headOfDepartment.getDepartment();
        } else {
            if(request.departmentId() == null) {
                throw new IllegalArgumentException("Department is required for this request");
            }
            return departmentRepository.findByIdOrThrowDefault(request.departmentId());
        }
    }
}
