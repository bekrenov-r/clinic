package com.bekrenov.clinic.service;

import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long departmentId) {
        return departmentRepository.findById(departmentId).get();
    }
}
