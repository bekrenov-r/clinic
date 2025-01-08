package com.bekrenov.clinic.security.auth.basic;

import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Person;
import com.bekrenov.clinic.repository.PersonRepository;
import com.bekrenov.clinic.security.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.bekrenov.clinic.security.auth.jwt.JwtProvider.DOCTOR_ID_CLAIM;
import static com.bekrenov.clinic.security.auth.jwt.JwtProvider.FIRST_NAME_CLAIM;

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
        return jwtProvider.generateToken(
                userDetailsService.loadUserByUsername(username),
                getClaims(person)
        );
    }

    private Map<String, Object> getClaims(Person person) {
        Map<String, Object> claims = new HashMap<>();
        if(person instanceof Doctor d) {
            claims.put(DOCTOR_ID_CLAIM, d.getId());
        }
        claims.put(FIRST_NAME_CLAIM, person.getFirstName());
        return claims;
    }
}
