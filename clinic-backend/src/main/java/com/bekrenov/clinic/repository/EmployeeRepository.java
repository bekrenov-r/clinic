package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason;
import com.bekrenov.clinic.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.EMPLOYEE_BY_EMAIL;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    default Employee findByEmailOrThrowDefault(String email) {
        return findByEmail(email).orElseThrow(() -> new ClinicEntityNotFoundException(EMPLOYEE_BY_EMAIL, email));
    }
}
