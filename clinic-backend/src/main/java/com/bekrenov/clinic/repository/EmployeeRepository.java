package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason;
import com.bekrenov.clinic.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.*;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("from Employee e where e.isDismissed = false")
    List<Employee> findAllActive();

    @Query("from Employee e where e.isDismissed = true")
    List<Employee> findAllDismissed();

    Optional<Employee> findByEmail(String email);
    default Employee findByEmailOrThrowDefault(String email) {
        return findByEmail(email).orElseThrow(() -> new ClinicEntityNotFoundException(EMPLOYEE_BY_EMAIL, email));
    }

    default Employee findByIdOrThrowDefault(Long id) {
        return findById(id).orElseThrow(() -> new ClinicEntityNotFoundException(EMPLOYEE, id));
    }
}
