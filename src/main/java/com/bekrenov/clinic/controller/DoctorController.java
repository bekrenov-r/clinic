package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.PersonDTO;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/profile")
    @Secured("DOCTOR")
    public ResponseEntity<DoctorDetailedResponse> getDoctorProfile(){
        return ResponseEntity.ok(doctorService.getDoctorProfile());
    }

    @GetMapping("/{id}")
    @Secured({"HEAD_OF_DEPARTMENT", "ADMIN"})
    public ResponseEntity<DoctorDetailedResponse> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/specialization")
    public ResponseEntity<List<PersonDTO>> getDoctorsBySpecialization(
            @RequestParam("spec") Department.Specialization specialization, HttpServletRequest request
    ){
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization, request));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<PersonDTO>> getDoctorsByDepartment(
            @PathVariable Long departmentId, HttpServletRequest request
    ){
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(departmentId, request));
    }
}
