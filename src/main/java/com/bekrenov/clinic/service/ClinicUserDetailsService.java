package com.bekrenov.clinic.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public interface ClinicUserDetailsService extends UserDetailsManager {

    UserDetails loadUserByUsername(String username);

    void changeUsername(String oldUsername, String newUsername);

    void authenticateUser(String username, String rawPassword, HttpServletRequest request);
}
