package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.entity.Registration;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PatientService {

    void save(Patient patient);

    void createPatientAndUser(Registration registration, HttpServletRequest request);

    Patient findById(int id);

    Patient findByEmail(String email);

    List<Patient> findAll(Sort sort);

    void deleteById(int id);

    boolean emailExists(String email);

    boolean phoneNumberExists(String phoneNumber);

    boolean peselExists(String pesel);

}
