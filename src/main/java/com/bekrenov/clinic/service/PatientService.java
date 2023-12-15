package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.dto.Registration;
import com.bekrenov.clinic.repository.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ClinicUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Value("${data.phone-number-prefix}")
    private String phoneNumberPrefix;

    public void createPatientAndUser(Registration registration, HttpServletRequest request) {
        Patient patient = registration.getPatient();
        // set id to null so that Hibernate creates new user
        patient.setId(null);
        // todo: fully remove 'username' field from patient, hook users to email

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

    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    public Patient findById(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.orElse(null);
    }

    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> findAll(Sort sort) {
        return patientRepository.findAll(sort);
    }

    public void deleteById(int id) {
        patientRepository.deleteById(id);
    }

    public boolean emailExists(String email) {
        return patientRepository.emailExists(email);
    }

    public boolean phoneNumberExists(String phoneNumber) {
        return patientRepository.phoneNumberExists(phoneNumber);
    }

    public boolean peselExists(String pesel) {
        return patientRepository.peselExists(pesel);
    }
}
