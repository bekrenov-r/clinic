package com.bekrenov.clinic.rest_controller;

import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/available-times-by-department")
    public List<LocalTime> getAvailableTimesByDepartment(@RequestParam("departmentId") int departmentId,
                                                         @RequestParam("date") LocalDate date){
     return appointmentService.getAvailableTimesByDepartment(departmentId, date);
    }

    @GetMapping("/available-times-by-doctor")
    public List<LocalTime> getAvailableTimesByDoctor(@RequestParam("doctorId") int doctorId,
                                                      @RequestParam("date") LocalDate date){
        return appointmentService.getAvailableTimesByDoctor(doctorId, date);
    }

}
