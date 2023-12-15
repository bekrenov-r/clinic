package com.bekrenov.clinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.bekrenov.clinic.service.DoctorService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "appointment_time")
    private LocalTime appointmentTime;

    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_department", referencedColumnName = "id")
    @JsonIgnore
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_doctor", referencedColumnName = "id")
    @JsonIgnore
    private Doctor doctor;

    @Transient
    private boolean isAnyDoctor;

    @Transient
    private LocalTime appointmentEndTime;

    @Transient
    private String formattedDate;

    @PostLoad
    public void setTransientFields(){
        appointmentEndTime = appointmentTime.plusMinutes(DoctorService.VISIT_DURATION_MINUTES);
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EE, d MMMM yyyy", new Locale("pl"));
        formattedDate = appointmentDate.format(formatter);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
    }
}

