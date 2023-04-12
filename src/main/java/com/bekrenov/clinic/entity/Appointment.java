package com.bekrenov.clinic.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "appointment_time")
//    @NotNull(message = "To pole jest wymagane")
    private LocalTime appointmentTime;

    @Column(name = "appointment_date")
//    @NotNull(message = "To pole jest wymagane")
    private LocalDate appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_department", referencedColumnName = "id")
    @JsonIgnore
//    @NotNull(message = "To pole jest wymagane")
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
    boolean anyDoctor;

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

