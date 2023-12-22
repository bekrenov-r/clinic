package com.bekrenov.clinic.service;

import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentService appointmentService;

    // duration for each visit in minutes
    public static final int VISIT_DURATION_MINUTES = 15;

    // time for beginning and ending of working day (by default 08:00-16:00)
    public static final LocalTime WORKING_DAY_BEGIN_TIME = LocalTime.of(8, 0);
    public static final LocalTime WORKING_DAY_END_TIME = LocalTime.of(16, 0);

    // time for beginning and ending of lunch break (by default 13:00-13:30)
    public static final LocalTime LUNCH_BREAK_BEGIN = LocalTime.of(13,0);
    public static final LocalTime LUNCH_BREAK_END = LocalTime.of(13, 30);

    public Doctor findById(Long id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> findDoctorsByDepartmentId(Long id) {
        return doctorRepository.findDoctorsByDepartment_Id(id);
    }

    public Doctor getAnyDoctorForAppointment(Appointment appointment) {
        List<Doctor> doctors = findDoctorsByDepartmentId(appointment.getDepartment().getId());
        for (Doctor doctor : doctors) {
            List<LocalTime> times = appointmentService.getAvailableTimesByDoctor(doctor.getId(), appointment.getDate());
            if (times.contains(appointment.getTime())) {
                return doctor;
            }
        }
        return null;
    }

}
