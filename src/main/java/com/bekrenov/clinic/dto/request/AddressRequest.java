package com.bekrenov.clinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(
        @NotBlank
        String city,
        @NotBlank
        String street,
        @NotBlank
        String buildingNumber,
        String flatNumber,
        @NotBlank @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "has invalid format (must be dd-ddd)")
        String zipCode
) { }
