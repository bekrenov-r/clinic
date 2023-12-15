package com.bekrenov.clinic.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class ClinicUserDetailsServiceImpl implements ClinicUserDetailsService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final AuthenticationManager authenticationManager;

    // todo: try setter injection here
    private final PasswordEncoder passwordEncoder;

    private static final String SPRING_SECURITY_CONTEXT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    public ClinicUserDetailsServiceImpl(JdbcUserDetailsManager jdbcUserDetailsManager, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
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

        JdbcTemplate jdbcTemplate = jdbcUserDetailsManager.getJdbcTemplate();

        jdbcTemplate.execute("set foreign_key_checks = 0");

        jdbcTemplate.update("update users set username = ? where username=?", newUsername, oldUsername);
        jdbcTemplate.update("update authorities set username = ? where username=?", newUsername, oldUsername);

        jdbcTemplate.execute("set foreign_key_checks = 1");
    }

    @Override
    public void authenticateUser(String username, String rawPassword, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, rawPassword);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
 /*       String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("changing password for user: " + username);
        UserDetails currentUser = jdbcUserDetailsManager.loadUserByUsername(username);
        System.out.println("old password: " + oldPassword);
        System.out.println("current password: " + currentUser.getPassword());
        passwordEncoder.matches(oldPassword, currentUser.getPassword());
        if(passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            jdbcUserDetailsManager.changePassword(oldPassword, passwordEncoder.encode(newPassword));
        } else {
            throw new InvalidPasswordException("Can't change password: given current password is invalid");
        }*/
//        jdbcUserDetailsManager.setAuthenticationManager(authenticationManager);
        String encodedPassword = passwordEncoder.encode(newPassword);
        jdbcUserDetailsManager.changePassword(oldPassword, encodedPassword);

    }

    @Override
    public boolean userExists(String username) {
        return jdbcUserDetailsManager.userExists(username);
    }

    @Override
    public boolean passwordIsCorrect(String username, String password) {
        UserDetails user = loadUserByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

}
