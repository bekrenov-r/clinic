package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason;
import com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason;
import com.bekrenov.clinic.model.entity.Person;
import com.bekrenov.clinic.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.*;

@Repository
@RequiredArgsConstructor
public class PersonRepository {
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    public Person findByEmailOrThrowDefault(String email) {
        UserDetails user = jdbcUserDetailsManager.loadUserByUsername(email);
        if(user == null){
            throw new ClinicEntityNotFoundException(USER, email);
        }
        try {
            return user.getAuthorities().contains(Role.PATIENT)
                    ? patientRepository.findByEmailOrThrowDefault(email)
                    : employeeRepository.findByEmailOrThrowDefault(email);
        } catch(ClinicEntityNotFoundException ex){
            throw new ClinicEntityNotFoundException(PERSON, email);
        }
    }
}
