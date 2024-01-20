package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DepartmentMapper;
import com.bekrenov.clinic.dto.response.DepartmentResponse;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

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
}
