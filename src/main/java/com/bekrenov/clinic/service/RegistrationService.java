package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DoctorMapper;
import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.request.DoctorRegistrationRequest;
import com.bekrenov.clinic.dto.request.PatientRegistrationRequest;
import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.security.Role;
import com.bekrenov.clinic.security.user.UserService;
import com.bekrenov.clinic.validation.RegistrationValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final UserService userService;
    private final RegistrationValidator registrationValidator;

    @Transactional
    public PatientResponse registerPatient(PatientRegistrationRequest request){
        registrationValidator.validateRegistrationRequest(request);
        userService.createUser(request, Set.of(Role.PATIENT), true);
        Patient patient = patientMapper.requestToEntity(request);
        return patientMapper.entityToResponse(patientRepository.save(patient));
    }

    @Transactional
    public DoctorDetailedResponse registerDoctor(DoctorRegistrationRequest request) {
        registrationValidator.validateRegistrationRequest(request);
        userService.createUser(request, Set.of(Role.DOCTOR), false);
        Doctor doctor = doctorMapper.requestToEntity(request);
        return doctorMapper.entityToDetailedResponse(doctorRepository.save(doctor));
    }
}
