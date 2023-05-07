package com.bekrenov.clinic.service;

import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class UserDetailsServiceImpl implements ClinicUserDetailsService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    public UserDetailsServiceImpl(JdbcUserDetailsManager jdbcUserDetailsManager) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return jdbcUserDetailsManager.loadUserByUsername(username);
    }

    @Override
    public void createUser(UserDetails user) {
        jdbcUserDetailsManager.createUser(user);
    }

    @Override
    public void updateUser(UserDetails updatedUser) {
    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    @Transactional
    public void changeUsername(String oldUsername, String newUsername) {
        System.out.println("changing username");

        JdbcTemplate jdbcTemplate = jdbcUserDetailsManager.getJdbcTemplate();

        jdbcTemplate.execute("set foreign_key_checks = 0");

        jdbcTemplate.update("update users set username = ? where username=?", newUsername, oldUsername);
        jdbcTemplate.update("update authorities set username = ? where username=?", newUsername, oldUsername);

        jdbcTemplate.execute("set foreign_key_checks = 1");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

}
