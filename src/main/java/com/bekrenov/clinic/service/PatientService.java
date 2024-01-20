package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final CurrentUserUtil currentUserUtil;
    private final PatientMapper patientMapper;

    public PatientResponse getPatientProfile() {
        Patient patient = patientRepository.findByEmail(currentUserUtil.getCurrentUser().getUsername());
        return patientMapper.entityToResponse(patient);
    }
}
