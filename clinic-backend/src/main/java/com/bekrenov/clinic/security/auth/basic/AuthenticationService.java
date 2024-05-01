package com.bekrenov.clinic.security.auth.basic;

import com.bekrenov.clinic.model.entity.Person;
import com.bekrenov.clinic.repository.PersonRepository;
import com.bekrenov.clinic.security.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PersonRepository personRepository;

    public String authenticate(String username, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Person person = personRepository.findByEmailOrThrowDefault(username);
        return jwtProvider.generateToken(userDetailsService.loadUserByUsername(username), person.getFirstName());
    }
}
