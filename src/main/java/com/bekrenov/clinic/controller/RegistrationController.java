package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.PatientRegistrationRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/patient")
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody PatientRegistrationRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrationService.registerPatient(request));
    }
}
