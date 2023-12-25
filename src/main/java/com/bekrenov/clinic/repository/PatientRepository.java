package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByEmail(String username);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPesel(String pesel);
}
