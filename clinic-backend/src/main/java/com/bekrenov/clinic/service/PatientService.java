package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.AddressMapper;
import com.bekrenov.clinic.dto.mapper.PatientMapper;
import com.bekrenov.clinic.dto.request.PatientRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.dto.response.PersonDTO;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Address;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.PATIENT_BY_EMAIL;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.PATIENT_BY_PESEL;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AddressService addressService;

    public PatientResponse getPatientProfile() {
        Patient patient = patientRepository.findByEmailOrThrowDefault(CurrentAuthUtil.getAuthentication().getName());
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

    public PersonDTO getPatientByPesel(String pesel) {
        Patient patient = patientRepository.findByPesel(pesel)
                .orElseThrow(() -> new ClinicEntityNotFoundException(PATIENT_BY_PESEL, pesel));
        return new PersonDTO(patient.getId(), patient.getFirstName(), patient.getLastName());
    }
}
