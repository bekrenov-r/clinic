package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService{

    private final PatientRepository repository;

    @Autowired
    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Patient patient) {
        repository.save(patient);
    }

    @Override
    public Patient findById(int id) {
        Optional<Patient> patient = repository.findById(id);
        return patient.orElse(null);
    }

    @Override
    public Patient findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public List<Patient> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }
}
