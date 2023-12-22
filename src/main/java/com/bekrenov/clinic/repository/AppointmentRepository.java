package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAppointmentsByDoctor_IdAndDate(Long id, LocalDate date);

}
