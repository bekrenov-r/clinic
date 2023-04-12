package com.bekrenov.clinic.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="department_name")
    private String departmentName;

    @Column(name = "specialization")
    private String specialization;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_address", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "department",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Doctor> doctors;

    public Department() {
    }

    public Department(String departmentName, String specialization, Address address) {
        this.departmentName = departmentName;
        this.specialization = specialization;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void addDoctor(Doctor doctor){
        if(doctors.isEmpty()){
            doctors = new ArrayList<>();
        }
        doctors.add(doctor);
    }

    @Override
    public String toString() {
        return departmentName + ", " + address.getStreet() + " " + address.getBuildingNumber();
    }

    public enum Specializations{
        PHYSICIAN("Terapeuta"),
        OPHTHALMOLOGIST("Okulistyka"),
        DERMATOLOGY("Dermatologia"),
        PSYCHOLOGY("Psychologia"),
        GASTROENTEROLOGY("Gastroenterologia");

        private String name;

        Specializations(String name){
            this.name = name;
        }

        public String toString(){
            return name;
        }
    }
}