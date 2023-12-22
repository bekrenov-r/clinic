package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.request.PatientRegistrationRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.model.enums.Role;
import com.bekrenov.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserService userService;

    public PatientResponse registerPatient(PatientRegistrationRequest request){
        assertEmailIsUnique(request.email());
        userService.createUser(request, Set.of(Role.PATIENT));
        Patient patient = patientMapper.requestToEntity(request);
        return patientMapper.entityToResponse(patientRepository.save(patient));
    }

    private void assertEmailIsUnique(String email){
        if(userService.existsByUsername(email))
            throw new RuntimeException("User with email " + email + "is already registered");
    }
}
