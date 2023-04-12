package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> findAll();

    Department findById(int departmentId);

    List<Department> findDepartmentsBySpecialization(String specialization);

}
