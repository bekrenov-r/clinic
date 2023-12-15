package com.bekrenov.clinic.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToOne(fetch = FetchType.EAGER,
                cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "patient",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public void addAppointment(Appointment appointment){
        if(appointments.isEmpty()){
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }

    public enum Gender {
        MALE,
        FEMALE;
    }
}
