package com.bekrenov.clinic.dto;

import com.bekrenov.clinic.entity.Patient;
import lombok.Data;

@Data
public class Registration {
    private Patient patient;
    private String password;
}
