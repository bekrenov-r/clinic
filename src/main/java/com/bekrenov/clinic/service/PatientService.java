package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Patient;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PatientService{

    void save(Patient patient);

    Patient findById(int id);

    Patient findByUsername(String username);

    List<Patient> findAll(Sort sort);

    void deleteById(int id);


}
