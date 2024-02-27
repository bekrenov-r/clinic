package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientResponse getPatientProfile() {
        Patient patient = patientRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        return patientMapper.entityToResponse(patient);
    }
}
