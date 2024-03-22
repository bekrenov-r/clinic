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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class DoctorIT {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestAuthenticator testAuthenticator;

    @Nested
    class GetDoctorProfile {
        private static final String RESPONSE_BODY_JSON = "/test/json/response/doctor-profile-response.json";

        @Test
        public void getDoctorProfile_Basic() throws JSONException {
            String authHeader = testAuthenticator.authenticateAsDoctor();
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/profile")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String expectedJson = TestUtil.getResourceAsString(RESPONSE_BODY_JSON);

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
            JSONAssert.assertEquals(expectedJson, response.getBody(), false);
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"PATIENT", "RECEPTIONIST", "ADMIN"})
        public void getDoctorProfile_ShouldReturn403_WhenRoleIsNotDoctor(Role role) {
            String authHeader = testAuthenticator.authenticateAs(role);
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/profile")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
        }
    }

    @Nested
    class GetDoctorById {
        private static final String RESPONSE_BODY_JSON = "/test/json/response/doctor-profile-response.json";
        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"ADMIN", "HEAD_OF_DEPARTMENT"})
        public void getDoctorById_Basic(Role role) throws JSONException {
            String authHeader = testAuthenticator.authenticateAs(role);
            Long doctorId = 2L;
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/{id}", doctorId)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String expectedJson = TestUtil.getResourceAsString(RESPONSE_BODY_JSON);

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
            JSONAssert.assertEquals(expectedJson, response.getBody(), false);
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"ADMIN", "HEAD_OF_DEPARTMENT"})
        public void getDoctorById_ShouldReturn404_WhenDoctorNotFound(Role role) {
            String authHeader = testAuthenticator.authenticateAs(role);
            Long doctorId = 111L;
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/{id}", doctorId)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(responseJson.read("$.status"), is(HttpStatus.NOT_FOUND.name()));
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"DOCTOR", "RECEPTIONIST", "PATIENT"})
        public void getDoctorById_ShouldReturn403_WhenRoleIsNotAllowed(Role role) {
            String authHeader = testAuthenticator.authenticateAs(role);
            Long doctorId = 1L;
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/{id}", doctorId)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
        }

        @Test
        public void getDoctorById_ShouldReturn403_WhenAuthHeaderIsNotPresent() {
            Long doctorId = 1L;
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/{id}", doctorId)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
            assertThat(responseJson.read("$.message"), is("Authorization header is invalid or is not present"));
        }

        @Test
        public void getDoctorById_ShouldReturn400_WhenHeadOfDepartmentRequestsDoctorFromAnotherDepartment(){
            String authHeader = testAuthenticator.authenticateAsHeadOfDepartment();
            Long doctorId = 3L;
            RequestEntity<Void> request = RequestEntity
                    .get("/doctors/{id}", doctorId)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }
    }
}
