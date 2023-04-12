package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    void save(Appointment appointment);

    Appointment findById(int id);

    void deleteById(int id);

    List<LocalTime> getAvailableTimesByDepartment(int departmentId, LocalDate date);

    List<LocalTime> getAvailableTimesByDoctor(int doctorId, LocalDate date);

}
