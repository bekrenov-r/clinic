package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.request.RegistrationRequest;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationValidator {
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public void validateRegistrationRequest(RegistrationRequest request){
        assertEmailIsUnique(request.email());
        assertPhoneNumberIsUnique(request.phoneNumber());
        assertPeselIsUnique(request.pesel());
    }

    private void assertEmailIsUnique(String email){
        if(userService.existsByUsername(email))
            throw new RuntimeException("User with email " + email + "is already registered");
    }

    private void assertPhoneNumberIsUnique(String phoneNumber){
        if(patientRepository.existsByPhoneNumber(phoneNumber) || doctorRepository.existsByPhoneNumber(phoneNumber))
            throw new RuntimeException("User with phone number " + phoneNumber + "is already registered");
    }

    private void assertPeselIsUnique(String pesel){
        if(patientRepository.existsByPesel(pesel) || doctorRepository.existsByPesel(pesel))
            throw new RuntimeException("User with pesel " + pesel + "is already registered");
    }
}
