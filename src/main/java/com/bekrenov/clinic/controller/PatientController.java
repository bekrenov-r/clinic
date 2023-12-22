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

    @GetMapping("/exists/email")
    public boolean emailExists(@RequestParam("email") String email){
        return patientService.emailExists(email);
    }

    @GetMapping("/exists/phone-number")
    public boolean phoneNumberExists(@RequestParam("phone-number") String phoneNumber){
        return patientService.phoneNumberExists(phoneNumber);
    }

    @GetMapping("/exists/pesel")
    public boolean peselExists(@RequestParam("pesel") String pesel){
        return patientService.peselExists(pesel);
    }

}
