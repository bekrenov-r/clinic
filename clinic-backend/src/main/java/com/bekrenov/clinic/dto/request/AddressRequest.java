package com.bekrenov.clinic.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressRequest(
        @NotBlank
        String city,
        @NotBlank
        String street,
        @NotBlank
        @JsonProperty("building")
        String buildingNumber,
        @JsonProperty("flat")
        String flatNumber,
        @NotBlank @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "has invalid format (must be dd-ddd)")
        String zipCode
) { }
