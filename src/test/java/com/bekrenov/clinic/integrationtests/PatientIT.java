package com.bekrenov.clinic.integrationtests;

import com.bekrenov.clinic.integrationtests.util.TestAuthenticator;
import com.bekrenov.clinic.integrationtests.util.TestUtil;
import com.bekrenov.clinic.security.Role;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PatientIT {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestAuthenticator testAuthenticator;

    private static final String RESPONSE_BODY_JSON = "/test/json/response/patient-profile-response.json";

    @Nested
    class GetPatientProfile {
        @Test
        public void getPatientProfile_basic() throws JSONException {
            String authHeader = testAuthenticator.authenticateAsPatient();
            RequestEntity<Void> request = RequestEntity
                    .get("/patients")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String expectedJson = TestUtil.getResourceAsString(RESPONSE_BODY_JSON);

            JSONAssert.assertEquals(expectedJson, response.getBody(), false);
        }

        @Test
        public void getPatientProfile_ShouldReturn403_WhenAuthHeaderIsNotPresent() {
            RequestEntity<Void> request = RequestEntity
                    .get("/patients")
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
            assertThat(responseJson.read("$.message"), is("Authorization header is invalid or is not present"));
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"DOCTOR", "HEAD_OF_DEPARTMENT", "RECEPTIONIST", "ADMIN"})
        public void getPatientProfile_ShouldReturn403_WhenRoleIsNotPatient(Role role) {
            String authHeader = testAuthenticator.authenticateAs(role);
            RequestEntity<Void> request = RequestEntity
                    .get("/patients")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
        }
    }
}
