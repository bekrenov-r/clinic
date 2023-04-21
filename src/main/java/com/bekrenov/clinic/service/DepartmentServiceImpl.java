package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findById(int departmentId) {
        return departmentRepository.findById(departmentId);
    }

//    @Override
//    public List<Department> findDepartmentsBySpecialization(String specialization) {
//        return departmentRepository.findDepartmentsBySpecialization(specialization);
//    }
}
