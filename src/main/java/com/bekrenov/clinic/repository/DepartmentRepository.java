package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findDepartmentsBySpecialization(String specialization);

    Department findById(int id);
}
