package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.entity.Registration;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PatientService{

    void save(Patient patient);

    void createPatientAndUser(Registration registration);

    Patient findById(int id);

    Patient findByUsername(String username);

    List<Patient> findAll(Sort sort);

    void deleteById(int id);


}
