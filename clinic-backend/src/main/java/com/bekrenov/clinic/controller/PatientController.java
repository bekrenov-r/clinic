package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.PatientRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.dto.response.PersonDTO;
import com.bekrenov.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @Secured("PATIENT")
    public ResponseEntity<PatientResponse> getPatientProfile(){
        return ResponseEntity.ok(patientService.getPatientProfile());
    }

    @GetMapping("/pesel/{pesel}")
    public ResponseEntity<PersonDTO> getPatientByPesel(@PathVariable("pesel") String pesel) {
        return ResponseEntity.ok(patientService.getPatientByPesel(pesel));
    }
}
