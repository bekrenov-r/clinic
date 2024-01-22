package com.bekrenov.clinic.service;

import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
}
