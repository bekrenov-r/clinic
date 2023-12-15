package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments/availability")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AppointmentService appointmentService;

    @GetMapping("/by-department")
    public List<LocalTime> getAvailableTimesByDepartment(@RequestParam("departmentId") Long departmentId,
                                                         @RequestParam("date") LocalDate date){
        return appointmentService.getAvailableTimesByDepartment(departmentId, date);
    }

    @GetMapping("/by-doctor")
    public List<LocalTime> getAvailableTimesByDoctor(@RequestParam("doctorId") Long doctorId,
                                                     @RequestParam("date") LocalDate date){
        return appointmentService.getAvailableTimesByDoctor(doctorId, date);
    }
}
