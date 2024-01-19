package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.response.PersonDto;
import com.bekrenov.clinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/specialization/{specializationId}")
    public ResponseEntity<List<? extends PersonDto>> getDoctorsBySpecialization(@PathVariable Long specializationId){
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specializationId));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<? extends PersonDto>> getDoctorsByDepartment(@PathVariable Long departmentId){
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(departmentId));
    }
}
