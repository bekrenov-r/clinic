package com.bekrenov.clinic.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "employees")
@DiscriminatorColumn(name = "employee_type")
@Data
public class Employee extends Person {
    @Column(name = "occupation")
    protected String occupation;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "id_department", referencedColumnName = "id")
    protected Department department;
}
