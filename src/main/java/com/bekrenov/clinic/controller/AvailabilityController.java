package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@RestController
@RequestMapping("/appointments/availability")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @GetMapping("/department")
    public ResponseEntity<Set<LocalTime>> getAvailableTimesByDepartment(
            @RequestParam("departmentId") Long departmentId,
            @RequestParam("date") LocalDate date
    ){
        return ResponseEntity.ok(availabilityService.getAvailableTimesByDepartment(departmentId, date));
    }

    @GetMapping("/doctor")
    public ResponseEntity<Set<LocalTime>> getAvailableTimesByDoctor(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("date") LocalDate date
    ){
        return ResponseEntity.ok(availabilityService.getAvailableTimesByDoctor(doctorId, date));
    }
}
