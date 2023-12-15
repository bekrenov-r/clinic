package com.bekrenov.clinic.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="addresses")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="city")
    private String city;

    @Column(name="street")
    private String street;

    @Column(name="building_number")
    private String buildingNumber;

    @Column(name="flat_number")
    private String flatNumber;

    @Column(name="postal_code")
    private String postalCode;
}
