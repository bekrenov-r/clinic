package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findDoctorsByDepartment_Id(int id);

    Doctor findById(int id);
}
