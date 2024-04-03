package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.DoctorRegistrationRequest;
import com.bekrenov.clinic.dto.request.PatientRequest;
import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.service.RegistrationService;
import com.bekrenov.clinic.validation.constraint.group.PatientRegistrationWithUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<PatientResponse> registerPatient(@RequestBody @Validated(PatientRegistrationWithUser.class) PatientRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrationService.registerPatient(request));
    }

    @PostMapping("/doctor")
    @Secured("HEAD_OF_DEPARTMENT")
    public ResponseEntity<DoctorDetailedResponse> registerDoctor(@RequestBody @Valid DoctorRegistrationRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registrationService.registerDoctor(request));
    }
}
