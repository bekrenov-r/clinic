package com.bekrenov.clinic.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name="addresses")
@Data
public class Address extends AbstractEntity {
    private static final String SIMPLE_ADDRESS_FORMAT = "ul. %s %s";

    @Column(name="city")
    private String city;

    @Column(name="street")
    private String street;

    @JsonProperty("building")
    @Column(name="building_number")
    private String buildingNumber;

    @JsonProperty("flat")
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
