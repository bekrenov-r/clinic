package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.AddressMapper;
import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.request.PatientRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Address;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AddressService addressService;

    public PatientResponse getPatientProfile() {
        Patient patient = patientRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        return patientMapper.entityToResponse(patient);
    }

    public Patient createPatient(PatientRequest request){
        Patient patient = patientMapper.requestToEntity(request);
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, PatientRequest request){
        Patient patient = patientRepository.findByIdOrThrowDefault(id);
        Address address = addressService.updateAddress(patient.getAddress().getId(), request.address());

        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setEmail(request.email());
        patient.setPhoneNumber(request.phoneNumber());
        patient.setAddress(address);

        return patientRepository.save(patient);
    }
}
