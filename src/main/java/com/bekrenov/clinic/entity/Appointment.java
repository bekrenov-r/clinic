package com.bekrenov.clinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.bekrenov.clinic.service.DoctorService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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
    private boolean anyDoctor;

    @Transient
    private LocalTime appointmentEndTime;

    @Transient
    private String formattedDate;

    public Appointment() {
    }

    public Appointment(LocalTime appointmentTime, LocalDate appointmentDate,
                       Department department, Patient patient, Doctor doctor,
                       boolean anyDoctor) {
        this.appointmentTime = appointmentTime;
        this.appointmentDate = appointmentDate;
        this.department = department;
        this.patient = patient;
        this.doctor = doctor;
        this.anyDoctor = anyDoctor;
    }

    @PostLoad
    public void setTransientFields(){
        appointmentEndTime = appointmentTime.plusMinutes(DoctorService.VISIT_DURATION_MINUTES);
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EE, d MMMM yyyy", new Locale("pl"));
        formattedDate = appointmentDate.format(formatter);
        formattedDate = formattedDate.substring(0, 1).toUpperCase() + formattedDate.substring(1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public boolean isAnyDoctor() {
        return anyDoctor;
    }

    public void setAnyDoctor(boolean anyDoctor) {
        this.anyDoctor = anyDoctor;
    }

    public LocalTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "\nid=" + id +
                ",\n appointmentTime=" + appointmentTime +
                ",\n appointmentDate=" + appointmentDate +
                ",\n department=" + department.getDepartmentName() +
                ",\n patient id=" + patient.getId() + "; username='" + patient.getUsername() + "'" +
                ",\n anyDoctor=" + anyDoctor +
                ",\n doctor id=" + doctor.getId() +
                "\n}";
    }
}

