package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.DoctorShortResponse;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.service.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    // todo: for roles DOCTOR and HEAD_OF_DEPARTMENT
    @GetMapping("/{id}")
    public ResponseEntity<DoctorDetailedResponse> getDoctorById(@PathVariable Long id){
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping("/specialization")
    public ResponseEntity<List<DoctorShortResponse>> getDoctorsBySpecialization(
            @RequestParam("spec") Department.Specialization specialization, HttpServletRequest request
    ){
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization, request));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DoctorShortResponse>> getDoctorsByDepartment(
            @PathVariable Long departmentId, HttpServletRequest request
    ){
        return ResponseEntity.ok(doctorService.getDoctorsByDepartment(departmentId, request));
    }
}
