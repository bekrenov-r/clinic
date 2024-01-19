package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByDepartment(Department department);
    @Query("from Doctor doctor where doctor.department.specialization = :specialization")
    List<Doctor> findBySpecialization(Specialization specialization);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPesel(String pesel);
}
