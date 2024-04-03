package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.AppointmentRequestByDoctor;
import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import com.bekrenov.clinic.dto.response.AppointmentResponse;
import com.bekrenov.clinic.dto.response.AppointmentShortResponse;
import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.bekrenov.clinic.service.AppointmentService;
import com.bekrenov.clinic.validation.constraint.group.PatientRegistrationWithUser;
import com.bekrenov.clinic.validation.constraint.group.PatientRegistrationWithoutUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    @Secured({"DOCTOR", "PATIENT"})
    public ResponseEntity<Page<AppointmentShortResponse>> getAllAppointmentsForCurrentUser(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "status", required = false) AppointmentStatus status
    ){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForCurrentUser(page, status));
    }

    @GetMapping("/{id}")
    @Secured({"DOCTOR", "PATIENT"})
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping("/doctor")
    @Secured("DOCTOR")
    public ResponseEntity<AppointmentResponse> createAppointmentAsDoctor(
            @RequestBody @Valid AppointmentRequestByDoctor request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointmentAsDoctor(request));
    }

    @PostMapping("/patient")
    @Secured("PATIENT")
    public ResponseEntity<AppointmentResponse> createAppointmentAsPatient(
            @RequestBody
            @Validated(PatientRegistrationWithoutUser.class)
            AppointmentRequestByPatient request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.createAppointmentAsPatient(request));
    }

    @PatchMapping("/{id}/confirm")
    @Secured({"DOCTOR", "RECEPTIONIST"})
    public ResponseEntity<Void> confirmAppointment(@PathVariable Long id){
        appointmentService.confirmAppointment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PatchMapping("/{id}/finish")
    @Secured("DOCTOR")
    public ResponseEntity<Void> finishAppointment(@PathVariable Long id){
        appointmentService.finishAppointment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}/cancel")
    @Secured("PATIENT")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id){
        appointmentService.cancelAppointment(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
