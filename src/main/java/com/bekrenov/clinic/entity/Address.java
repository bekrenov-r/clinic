package com.bekrenov.clinic.entity;

import jakarta.persistence.*;

@Entity
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

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

    public Address() {
    }

    public Address(String city, String street, String buildingNumber, String flatNumber, String postalCode) {
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.flatNumber = flatNumber;
        this.postalCode = postalCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getFlatNumber() {
        return flatNumber;

    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ",\n city='" + city + '\'' +
                ",\n street='" + street + '\'' +
                ",\n buildingNumber='" + buildingNumber + '\'' +
                ",\n flatNumber='" + flatNumber + '\'' +
                ",\n postalCode='" + postalCode + '\'' +
                '}';
    }
}
