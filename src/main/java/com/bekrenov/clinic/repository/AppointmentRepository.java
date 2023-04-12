package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findAppointmentsByDoctor_IdAndAppointmentDate(int id, LocalDate date);

}
