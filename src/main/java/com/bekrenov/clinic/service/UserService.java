package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.request.RegistrationRequest;
import com.bekrenov.clinic.model.entity.ActivationToken;
import com.bekrenov.clinic.model.enums.Role;
import com.bekrenov.clinic.repository.ActivationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ActivationTokenRepository activationTokenRepository;

    private static final String SPRING_SECURITY_CONTEXT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    public void createUser(RegistrationRequest request, Set<Role> roles){
        Set<GrantedAuthority> authorities = roles.stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        UserDetails user = User.builder()
                .username(request.email())
                .password(passwordEncoder.encode(request.password()))
                .authorities(authorities)
                .disabled(true)
                .build();
        userDetailsManager.createUser(user);
        createActivationTokenForUser(user);
    }

    public void authenticateUser(String username, String rawPassword, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, rawPassword);
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        userDetailsManager.changePassword(oldPassword, encodedPassword);

    }

    public boolean existsByUsername(String username){
        return userDetailsManager.userExists(username);
    }


    public void activateUser(String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token)
                .orElseThrow(RuntimeException::new);
        UserDetails user = userDetailsManager.loadUserByUsername(activationToken.getUsername());
        UserDetails activatedUser = User
                .withUserDetails(user)
                .disabled(false)
                .build();
        userDetailsManager.updateUser(activatedUser);
    }

    private void createActivationTokenForUser(UserDetails user){
        String token = RandomStringUtils.random(20, true, true);
        ActivationToken activationToken = ActivationToken.builder()
                .token(token)
                .username(user.getUsername())
                .build();
        activationTokenRepository.save(activationToken);
    }
}
