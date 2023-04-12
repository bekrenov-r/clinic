package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Appointment;
import com.bekrenov.clinic.entity.Doctor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorService {

    // duration for each visit in minutes
    int VISIT_DURATION_MINUTES = 15;

    // time for beginning and ending of working day (by default 08:00-16:00)
    LocalTime WORKING_DAY_BEGIN_TIME = LocalTime.of(8, 0);
    LocalTime WORKING_DAY_END_TIME = LocalTime.of(16, 0);

    // time for beginning and ending of lunch break (by default 13:00-13:30)
    LocalTime LUNCH_BREAK_BEGIN = LocalTime.of(13,0);
    LocalTime LUNCH_BREAK_END = LocalTime.of(13, 30);

    List<Doctor> findDoctorsByDepartmentId(int id);

    Doctor findById(int id);

    Doctor getAnyDoctorForAppointment(Appointment appointment);

}
