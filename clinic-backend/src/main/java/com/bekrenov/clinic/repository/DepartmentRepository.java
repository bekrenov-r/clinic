package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllBySpecialization(Department.Specialization specialization);
    boolean existsByName(String name);

    default Department findByIdOrThrowDefault(Long id) {
        return findById(id).orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
    }
}
