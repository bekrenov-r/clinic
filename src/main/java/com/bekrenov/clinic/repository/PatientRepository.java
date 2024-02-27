package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.PATIENT;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByEmail(String username);

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByPesel(String pesel);

    default Patient findByIdOrThrowDefault(Long id) {
        return findById(id).orElseThrow(() -> new ClinicEntityNotFoundException(PATIENT, id));
    }
}
