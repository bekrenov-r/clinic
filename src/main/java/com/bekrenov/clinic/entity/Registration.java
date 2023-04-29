package com.bekrenov.clinic.entity;

public class Registration {

    private Patient patient;

    private String password;

    public Registration() {
    }

    public Registration(Patient patient, String password) {
        this.patient = patient;
        this.password = password;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
