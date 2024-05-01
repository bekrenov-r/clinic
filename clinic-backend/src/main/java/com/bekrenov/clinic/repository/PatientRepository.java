package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.PATIENT;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.PATIENT_BY_EMAIL;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPesel(String pesel);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPesel(String pesel);
    boolean existsByEmail(String email);

    default Patient findByIdOrThrowDefault(Long id) {
        return findById(id).orElseThrow(() -> new ClinicEntityNotFoundException(PATIENT, id));
    }

    default Patient findByEmailOrThrowDefault(String email) {
        return findByEmail(email).orElseThrow(() -> new ClinicEntityNotFoundException(PATIENT_BY_EMAIL, email));
    }
}
