package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.entity.Registration;
import com.bekrenov.clinic.repository.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService{

    private final PatientRepository repository;
    private final ClinicUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Value("${data.phone-number-prefix}")
    private String phoneNumberPrefix;

    @Autowired
    public PatientServiceImpl(PatientRepository repository, ClinicUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createPatientAndUser(Registration registration, HttpServletRequest request) {
        Patient patient = registration.getPatient();
        // set id to 0 so that Hibernate creates new user
        patient.setId(0);
        // todo: fully remove 'username' field from patient, hook users to email
        patient.setUsername(patient.getEmail());

        patient.setPhoneNumber(phoneNumberPrefix + patient.getPhoneNumber());
        // save patient to database
        save(patient);

        UserDetails user = User.builder()
                        .username(registration.getPatient().getEmail())
                        .password(passwordEncoder.encode(registration.getPassword()))
                        .roles("PATIENT")
                        .disabled(false)
                        .build();
        userDetailsService.createUser(user);
        userDetailsService.authenticateUser(user.getUsername(), registration.getPassword(), request);
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
