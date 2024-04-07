package com.bekrenov.clinic.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("DOCTOR")
@Data
public class Doctor extends Employee {
    @OneToMany(mappedBy = "doctor", // "doctor" references to field name in appointment entity class
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private List<Appointment> appointments;
}
