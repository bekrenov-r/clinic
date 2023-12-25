package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findDoctorsByDepartment_Id(Long id);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPesel(String pesel);
}
