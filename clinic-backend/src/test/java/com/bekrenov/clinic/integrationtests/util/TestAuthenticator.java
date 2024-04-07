package com.bekrenov.clinic.integrationtests.util;

import com.bekrenov.clinic.security.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TestAuthenticator {
    private static final String ADMIN_USERNAME = "jan.kowalski@example.com";
    private static final String ADMIN_PASSWORD = "pass";
    private static final String PATIENT_USERNAME = "zbigniew.reszka@gmail.com";
    private static final String PATIENT_PASSWORD = "pass";
    private static final String DOCTOR_USERNAME = "piotr.raczkowski@gmail.com";
    private static final String DOCTOR_PASSWORD = "pass";
    private static final String HEAD_OF_DEPARTMENT_USERNAME = "marta.stachyra@gmail.com";
    private static final String HEAD_OF_DEPARTMENT_PASSWORD = "pass";
    private static final String RECEPTIONIST_USERNAME = "zuzanna.majchrzak@gmail.com";
    private static final String RECEPTIONIST_PASSWORD = "pass";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${test.server.baseUrl}")
    private String baseUrl;
    private final RestTemplate restTemplate;

    public TestAuthenticator(){
        restTemplate = new RestTemplate();
    }

    public String authenticateAs(Role role){
        return switch(role){
            case ADMIN -> authenticateAsAdmin();
            case PATIENT -> authenticateAsPatient();
            case DOCTOR -> authenticateAsDoctor();
            case HEAD_OF_DEPARTMENT -> authenticateAsHeadOfDepartment();
            case RECEPTIONIST -> authenticateAsReceptionist();
            default -> throw new UnsupportedOperationException();
        };
    }

    public String authenticateAsAdmin() {
        return authenticate(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public String authenticateAsPatient() {
        return authenticate(PATIENT_USERNAME, PATIENT_PASSWORD);
    }

    public String authenticateAsDoctor() {
        return authenticate(DOCTOR_USERNAME, DOCTOR_PASSWORD);
    }

    public String authenticateAsHeadOfDepartment() {
        return authenticate(HEAD_OF_DEPARTMENT_USERNAME, HEAD_OF_DEPARTMENT_PASSWORD);
    }

    public String authenticateAsReceptionist(){
        return authenticate(RECEPTIONIST_USERNAME, RECEPTIONIST_PASSWORD);
    }

    private String authenticate(String username, String password){
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/authenticate?username={username}&password={password}",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class,
                username, password
        );
        return BEARER_PREFIX + response.getBody();
    }
}
