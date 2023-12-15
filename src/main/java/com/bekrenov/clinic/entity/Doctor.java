package com.bekrenov.clinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "position")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_department", referencedColumnName = "id")
    @JsonIgnore
    private Department department;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "doctor", // "doctor" references to field name in appointment entity class
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @Transient
    private String firstAndLastName;

    public void addAppointment(Appointment appointment){
        if(appointments.isEmpty()){
            appointments = new ArrayList<>();
        }
        appointments.add(appointment);
    }
}
