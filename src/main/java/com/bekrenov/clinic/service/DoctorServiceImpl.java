package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Appointment;
import com.bekrenov.clinic.entity.Doctor;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, AppointmentService appointmentService) {
        this.doctorRepository = doctorRepository;
        this.appointmentService = appointmentService;
    }

    @Override
    public Doctor findById(int id) {
        return doctorRepository.findById(id);
    }

    @Override
    public List<Doctor> findDoctorsByDepartmentId(int id) {
        return doctorRepository.findDoctorsByDepartment_Id(id);
    }

    @Override
    public Doctor getAnyDoctorForAppointment(Appointment appointment) {
        List<Doctor> doctors = findDoctorsByDepartmentId(appointment.getDepartment().getId());
        for (Doctor doctor : doctors) {
            List<LocalTime> times = appointmentService.getAvailableTimesByDoctor(doctor.getId(), appointment.getAppointmentDate());
            if (times.contains(appointment.getAppointmentTime())) {
                return doctor;
            }
        }
        return null;
    }

}
