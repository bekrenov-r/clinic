package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllBySpecialization(Department.Specialization specialization);
}
