package com.bekrenov.clinic.dto.request;

// todo: add validation
public record AddressRequest(
        String city,
        String street,
        String buildingNumber,
        String flatNumber,
        String zipCode
) { }
