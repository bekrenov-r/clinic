package com.bekrenov.clinic.integrationtests;

import com.bekrenov.clinic.integrationtests.util.TestAuthenticator;
import com.bekrenov.clinic.integrationtests.util.TestUtil;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.security.Role;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RegistrationIT {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestAuthenticator testAuthenticator;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Nested
    class RegisterPatient {
        private static final String REQUEST_BODY_JSON = "/test/json/request/patient-registration-request.json";
        private static final String RESPONSE_BODY_JSON = "/test/json/response/patient-registration-response.json";
        @Test
        public void registerPatient_Basic() throws JSONException {
            RequestEntity<String> request = RequestEntity
                    .post("/register/patient")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(TestUtil.getResourceAsString(REQUEST_BODY_JSON));

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String expectedResponse = new JSONObject(TestUtil.getResourceAsString(RESPONSE_BODY_JSON)).toString();

            assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
            assertNotNull(response.getBody());
            JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
        }

        @Test
        public void registerPatient_InvalidRequestBody() throws JSONException {
            Map<String, String> invalidProperties = new HashMap<>();
            invalidProperties.put("firstName", "");
            invalidProperties.put("lastName", "");
            invalidProperties.put("pesel", "123");
            invalidProperties.put("email", "invalid_email");
            invalidProperties.put("password", "123");
            invalidProperties.put("address", null);
            String requestBody = TestUtil.prepareRequestBody(REQUEST_BODY_JSON, invalidProperties);
            RequestEntity<String> request = RequestEntity
                    .post("/register/patient")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }

        @Test
        public void registerPatient_ShouldReturn409_WhenEmailAlreadyExists() throws JSONException {
            String requestBody = TestUtil
                    .prepareRequestBody(REQUEST_BODY_JSON, "email", getExistingEmail());
            RequestEntity<String> request = RequestEntity
                    .post("/register/patient")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        @Test
        public void registerPatient_ShouldReturn409_WhenPhoneNumberAlreadyExists() throws JSONException {
            String requestBody = TestUtil
                    .prepareRequestBody(REQUEST_BODY_JSON, "phoneNumber", getExistingPhoneNumber());
            RequestEntity<String> request = RequestEntity
                    .post("/register/patient")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        @Test
        public void registerPatient_ShouldReturn409_WhenPeselAlreadyExists() throws JSONException {
            String requestBody = TestUtil.prepareRequestBody(REQUEST_BODY_JSON, "pesel", getExistingPESEL());
            RequestEntity<String> request = RequestEntity
                    .post("/register/patient")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        private String getExistingPhoneNumber(){
            return patientRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No patients found"))
                    .getPhoneNumber();
        }

        private String getExistingEmail(){
            return patientRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No patients found"))
                    .getEmail();
        }

        private String getExistingPESEL(){
            return patientRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No patients found"))
                    .getPesel();
        }
    }

    @Nested
    class RegisterDoctor {
        private static final String REQUEST_BODY_JSON = "/test/json/request/doctor-registration-request.json";
        private static final String RESPONSE_BODY_JSON = "/test/json/response/doctor-registration-response.json";

        @Test
        public void registerDoctor_Basic() throws JSONException {
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsHeadOfDepartment())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(TestUtil.getResourceAsString(REQUEST_BODY_JSON));

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String expectedResponse = new JSONObject(TestUtil.getResourceAsString(RESPONSE_BODY_JSON)).toString();

            assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
            assertNotNull(response.getBody());
            JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
        }

        @Test
        public void registerDoctor_InvalidRequestBody() throws JSONException {
            Map<String, String> invalidProperties = new HashMap<>();
            invalidProperties.put("firstName", "");
            invalidProperties.put("lastName", "");
            invalidProperties.put("phoneNumber", "123");
            invalidProperties.put("pesel", "123");
            invalidProperties.put("email", "invalid_email");
            invalidProperties.put("password", "123");
            invalidProperties.put("occupation", "");
            invalidProperties.put("departmentId", "");
            invalidProperties.put("address", null);
            String requestBody = TestUtil.prepareRequestBody(REQUEST_BODY_JSON, invalidProperties);
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsHeadOfDepartment())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }

        @Test
        public void registerDoctor_ShouldReturn403_WhenAuthHeaderIsNotPresent(){
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(TestUtil.getResourceAsString(REQUEST_BODY_JSON));

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
            assertThat(responseJson.read("$.message"), is("Authorization header is invalid or is not present"));
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"ADMIN", "PATIENT", "DOCTOR"})
        public void registerDoctor_ShouldReturn403_WhenRoleIsNotHeadOfDepartment(Role role){
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAs(role))
                    .body(TestUtil.getResourceAsString(REQUEST_BODY_JSON));

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
        }

        @Test
        public void registerDoctor_ShouldReturn404_WhenDepartmentDoesNotExist() throws JSONException {
            String requestBody = TestUtil.prepareRequestBody(REQUEST_BODY_JSON, "departmentId", 111);
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsHeadOfDepartment())
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(responseJson.read("$.status"), is(HttpStatus.NOT_FOUND.name()));
        }

        @Test
        public void registerDoctor_ShouldReturn409_WhenEmailAlreadyExists() throws JSONException {
            String requestBody = TestUtil
                    .prepareRequestBody(REQUEST_BODY_JSON, "email", getExistingEmail());
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsHeadOfDepartment())
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        @Test
        public void registerDoctor_ShouldReturn409_WhenPhoneNumberAlreadyExists() throws JSONException {
            String requestBody = TestUtil
                    .prepareRequestBody(REQUEST_BODY_JSON, "phoneNumber", getExistingPhoneNumber());
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsHeadOfDepartment())
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        @Test
        public void registerDoctor_ShouldReturn409_WhenPeselAlreadyExists() throws JSONException {
            String requestBody = TestUtil.prepareRequestBody(REQUEST_BODY_JSON, "pesel", getExistingPESEL());
            RequestEntity<String> request = RequestEntity
                    .post("/register/doctor")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsHeadOfDepartment())
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        private String getExistingPhoneNumber(){
            return doctorRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No doctors found"))
                    .getPhoneNumber();
        }

        private String getExistingEmail(){
            return doctorRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No doctors found"))
                    .getEmail();
        }

        private String getExistingPESEL(){
            return doctorRepository.findAll()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("No doctors found"))
                    .getPesel();
        }
    }
}
