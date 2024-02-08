package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDoctorAndDate(Doctor doctor, LocalDate date);
    List<Appointment> findAllByDepartment(Department department);
    boolean existsByPatientAndDateAndTime(Patient patient, LocalDate date, LocalTime time);
}
