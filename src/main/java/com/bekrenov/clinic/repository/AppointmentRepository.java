package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByDoctorAndDate(Doctor doctor, LocalDate date);
    List<Appointment> findAllByDoctor(Doctor doctor);
    List<Appointment> findAllByPatient(Patient patient);
    @Query("from Appointment a where a.status = 'CONFIRMED'")
    List<Appointment> findAllWithStatusConfirmed();
}
