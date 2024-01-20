package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
