package com.bekrenov.clinic.rest_controller;

import com.bekrenov.clinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientRestController {

    private final PatientService patientService;

    @Autowired
    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/email-exists")
    public boolean emailExists(@RequestParam("email") String email){
        return patientService.emailExists(email);
    }

    @GetMapping("/phone-number-exists")
    public boolean phoneNumberExists(@RequestParam("phone-number") String phoneNumber){
        return patientService.phoneNumberExists(phoneNumber);
    }

    @GetMapping("/pesel-exists")
    public boolean peselExists(@RequestParam("pesel") String pesel){
        return patientService.peselExists(pesel);
    }

}
