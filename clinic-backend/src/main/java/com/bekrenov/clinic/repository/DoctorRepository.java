package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DOCTOR;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDepartment(Department department);
    @Query("from Doctor doctor where doctor.department.specialization = :specialization")
    List<Doctor> findBySpecialization(Department.Specialization specialization);
    Doctor findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPesel(String pesel);

    default Doctor findByIdOrThrowDefault(Long id) {
        return findById(id).orElseThrow(() -> new ClinicEntityNotFoundException(DOCTOR, id));
    }
}
