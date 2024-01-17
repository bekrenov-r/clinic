package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.request.PatientRegistrationRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.model.enums.Role;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.security.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserService userService;
    private final RegistrationValidator registrationValidator;

    @Transactional
    public PatientResponse registerPatient(PatientRegistrationRequest request){
        registrationValidator.validateRegistrationRequest(request);
        userService.createUser(request, Set.of(Role.PATIENT));
        Patient patient = patientMapper.requestToEntity(request);
        return patientMapper.entityToResponse(patientRepository.save(patient));
    }
}
