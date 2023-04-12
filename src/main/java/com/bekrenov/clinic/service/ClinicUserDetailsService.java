package com.bekrenov.clinic.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;

public interface ClinicUserDetailsService extends UserDetailsManager {

    UserDetails loadUserByUsername(String username);

    void changeUsername(String oldUsername, String newUsername);
}
