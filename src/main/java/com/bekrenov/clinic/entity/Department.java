package com.bekrenov.clinic.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="department_name")
    private String departmentName;

    @Column(name = "specialization")
    private String specialization;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "department",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Doctor> doctors;

    public void addDoctor(Doctor doctor){
        if(doctors.isEmpty()){
            doctors = new ArrayList<>();
        }
        doctors.add(doctor);
    }


    @Getter
    @AllArgsConstructor
    public enum Specializations{
        PHYSICIAN("Terapeuta"),
        OPHTHALMOLOGIST("Okulistyka"),
        DERMATOLOGY("Dermatologia"),
        PSYCHOLOGY("Psychologia"),
        GASTROENTEROLOGY("Gastroenterologia");

        private final String name;
    }
}