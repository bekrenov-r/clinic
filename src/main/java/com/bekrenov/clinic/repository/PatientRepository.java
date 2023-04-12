package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByUsername(String username);


}
