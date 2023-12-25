package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<Patient> getPatientProfile(){
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<Patient> registerPatient(){
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
        return null;
    }

}
