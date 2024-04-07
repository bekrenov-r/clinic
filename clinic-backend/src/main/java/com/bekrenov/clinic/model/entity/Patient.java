package com.bekrenov.clinic.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "patients")
@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends Person {
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @OneToMany(mappedBy = "patient",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}
