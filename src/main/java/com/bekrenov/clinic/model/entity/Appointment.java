package com.bekrenov.clinic.model.entity;

import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.bekrenov.clinic.service.DoctorService;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "appointment_time")
    private LocalTime time;

    @Column(name = "appointment_date")
    private LocalDate date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "prescription")
    private String prescription;

    @Column(name = "details")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_department", referencedColumnName = "id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    private Doctor doctor;

    @Transient
    private boolean isAnyDoctor;

    @Transient
    private LocalTime appointmentEndTime;

    @Transient
    private String formattedDate;

    @PostLoad
    public void setTransientFields(){
        appointmentEndTime = time.plusMinutes(DoctorService.VISIT_DURATION_MINUTES);
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EE, d MMMM yyyy", new Locale("pl"));
        formattedDate = date.format(formatter);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
    }
}

