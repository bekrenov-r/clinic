package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.entity.Appointment;
import com.bekrenov.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointmentsForCurrentUser(){
        return null;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(){
        return null;
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id){
        return null;
    }
}
