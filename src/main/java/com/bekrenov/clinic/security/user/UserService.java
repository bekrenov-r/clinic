package com.bekrenov.clinic.security.user;

import com.bekrenov.clinic.dto.request.RegistrationRequest;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.ActivationToken;
import com.bekrenov.clinic.model.enums.Role;
import com.bekrenov.clinic.repository.ActivationTokenRepository;
import com.bekrenov.clinic.util.MailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.ACTIVATION_TOKEN;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ActivationTokenRepository activationTokenRepository;
    private final MailService mailService;

    public void createUser(RegistrationRequest request, Set<Role> roles, boolean needsActivation) {
        UserDetails user = User.builder()
                .username(request.email())
                .password(passwordEncoder.encode(request.password()))
                .authorities(parseAuthorities(roles))
                .disabled(needsActivation)
                .build();
        userDetailsManager.createUser(user);
        String activationToken = createActivationTokenForUser(user);
        if(needsActivation)
            mailService.sendEmailWithActivationLink(request, activationToken);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        userDetailsManager.changePassword(oldPassword, encodedPassword);

    }

    public boolean existsByUsername(String username){
        return userDetailsManager.userExists(username);
    }

    @Transactional
    public void activateUser(String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ClinicEntityNotFoundException(ACTIVATION_TOKEN, token));
        UserDetails user = userDetailsManager.loadUserByUsername(activationToken.getUsername());
        UserDetails activatedUser = User
                .withUserDetails(user)
                .disabled(false)
                .build();
        userDetailsManager.updateUser(activatedUser);
        activationTokenRepository.delete(activationToken);
    }

    private String createActivationTokenForUser(UserDetails user){
        String token = RandomStringUtils.random(20, true, true);
        ActivationToken activationToken = ActivationToken.builder()
                .token(token)
                .username(user.getUsername())
                .build();
        activationTokenRepository.save(activationToken);
        return token;
    }

    private Set<GrantedAuthority> parseAuthorities(Set<Role> roles){
        return roles.stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
