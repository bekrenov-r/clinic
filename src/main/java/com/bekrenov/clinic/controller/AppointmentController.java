package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.AppointmentRequestByDoctor;
import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import com.bekrenov.clinic.dto.response.AppointmentResponse;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    @PostMapping("/doctor")
    @Secured("DOCTOR")
    public ResponseEntity<AppointmentResponse> createAppointmentAsDoctor(@RequestBody AppointmentRequestByDoctor request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointmentAsDoctor(request));
    }

    @PostMapping("/patient")
    @Secured("PATIENT")
    public ResponseEntity<AppointmentResponse> createAppointmentAsPatient(@RequestBody AppointmentRequestByPatient request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointmentAsPatient(request));
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id){
        return null;
    }
}
