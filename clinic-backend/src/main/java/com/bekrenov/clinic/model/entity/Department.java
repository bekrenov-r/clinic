package com.bekrenov.clinic.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Entity
@Table(name = "departments")
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends AbstractEntity {
    @Column(name="name")
    private String name;

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
    private List<Employee> employees;

    public String nameAndAddress(){
        return name + ", " + address.toSimpleString();
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