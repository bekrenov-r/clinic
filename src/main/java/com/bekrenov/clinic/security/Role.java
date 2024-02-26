package com.bekrenov.clinic.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PATIENT,
    DOCTOR,
    RECEPTIONIST,
    HEAD_OF_DEPARTMENT,
    ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
