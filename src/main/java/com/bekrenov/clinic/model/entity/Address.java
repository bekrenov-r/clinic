package com.bekrenov.clinic.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name="addresses")
@Data
public class Address {
    private static final String SIMPLE_ADDRESS_FORMAT = "ul. %s %s";

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

    @Column(name="zip_code")
    private String zipCode;

    public String toSimpleString(){
        return String.format(SIMPLE_ADDRESS_FORMAT, street, buildingNumber);
    }

    @Override
    public boolean equals(Object other) {
        if(other == this)
            return true;
        if(!(other instanceof Address address))
            return false;
        return Objects.equals(this.city, address.getCity()) &&
                Objects.equals(this.street, address.getStreet()) &&
                Objects.equals(this.buildingNumber, address.getBuildingNumber()) &&
                Objects.equals(this.flatNumber, address.getFlatNumber()) &&
                Objects.equals(this.zipCode, address.getZipCode());
    }
}
