package com.bekrenov.clinic.rest_controller;

import com.bekrenov.clinic.entity.Doctor;
import com.bekrenov.clinic.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorRestController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorRestController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // get all doctors working in given department
    @GetMapping("/bydepartment/{departmentId}")
    public List<Doctor> getDoctorsByDepartment(@PathVariable int departmentId){
        return doctorService.findDoctorsByDepartmentId(departmentId);
    }

}
