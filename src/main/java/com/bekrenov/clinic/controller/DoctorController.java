package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.entity.Doctor;
import com.bekrenov.clinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/bydepartment/{departmentId}")
    public List<Doctor> getDoctorsByDepartment(@PathVariable Long departmentId){
        return doctorService.findDoctorsByDepartmentId(departmentId);
    }

}
