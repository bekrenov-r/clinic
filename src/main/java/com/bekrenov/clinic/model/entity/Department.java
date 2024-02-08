package com.bekrenov.clinic.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

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
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @Column(name = "auto_confirm_appointment")
    private Boolean autoConfirmAppointment;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "department",
            fetch = FetchType.LAZY)
    private List<Doctor> doctors;

    public String nameAndAddress(){
        return departmentName + ", " + address.toSimpleString();
    }

    @Getter
    @AllArgsConstructor
    public enum Specialization {
        PHYSICIAN("Terapeuta"),
        OPHTHALMOLOGY("Okulistyka"),
        DERMATOLOGY("Dermatologia"),
        PSYCHOLOGY("Psychologia"),
        GASTROENTEROLOGY("Gastroenterologia");

        private final String name;
    }
}