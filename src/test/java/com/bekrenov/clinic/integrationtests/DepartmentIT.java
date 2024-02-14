package com.bekrenov.clinic.integrationtests;

import com.bekrenov.clinic.integrationtests.util.JSONUtil;
import com.bekrenov.clinic.integrationtests.util.TestAuthenticator;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.security.Role;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class DepartmentIT {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestAuthenticator testAuthenticator;

    @Nested
    class GetAllDepartments {
        @Test
        public void getAllDepartments_WithoutSpecialization(){
            RequestEntity<Void> request = RequestEntity.get("/departments").build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            String responseBody = response.getBody();

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
            assertNotNull(responseBody);
        }

        @ParameterizedTest
        @EnumSource(Department.Specialization.class)
        public void getAllDepartments_WithSpecialization(Department.Specialization specialization){
            ResponseEntity<String> response = restTemplate.exchange(
                    "/departments?spec={spec}", HttpMethod.GET, HttpEntity.EMPTY, String.class, specialization.name()
            );
            String responseBody = response.getBody();

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
            assertNotNull(responseBody);
        }

        @Test
        public void getAllDepartments_WithInvalidSpecialization_ShouldReturn400(){
            String specialization = "asd";

            ResponseEntity<?> response = restTemplate.exchange(
                    "/departments?spec={spec}", HttpMethod.GET, HttpEntity.EMPTY, String.class, specialization
            );

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        }
    }

    @Nested
    class CreateDepartment {
        private static final String JSON_PATH = "/test/json/department-request.json";

        @Test
        public void createDepartment_Basic() {
            String authHeader = testAuthenticator.authenticateAsAdmin();
            RequestEntity<String> request = RequestEntity
                    .post("/departments")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(validRequestBody());

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
            assertNotNull(response.getBody());
            assertNotNull(responseJson.read("$.id"));
            assertThat(responseJson.read("$.name"), is("Oddział psychologii №2"));
            assertThat(responseJson.read("$.specialization"), is("PSYCHOLOGY"));
            assertThat(responseJson.read("$.autoConfirmAppointment"), is(true));
            assertNotNull(responseJson.read("$.address"));
        }

        @Test
        public void createDepartment_InvalidRequestBody() throws JSONException {
            String authHeader = testAuthenticator.authenticateAsAdmin();
            String requestBody = prepareRequestBody(Map.of(
                    "name", "",
                    "specialization", "asd",
                    "address.zipCode", "1234"));
            RequestEntity<String> request = RequestEntity
                    .post("/departments")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertNotNull(response.getBody());
            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        public void createDepartment_ShouldReturn403_WhenAuthHeaderIsNotPresent(){
            String requestBody = "{}";
            RequestEntity<String> request = RequestEntity
                    .post("/departments")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertNotNull(response.getBody());
            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
            assertThat(responseJson.read("$.message"), is("Authorization header is invalid or is not present"));
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"PATIENT", "HEAD_OF_DEPARTMENT", "DOCTOR"})
        public void createDepartment_ShouldReturn403_WhenRoleIsNotAdmin(Role role){
            String authHeader = testAuthenticator.authenticateAs(role);
            RequestEntity<String> request = RequestEntity
                    .post("/departments")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(validRequestBody());

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
        }

        @Test
        public void createDepartment_ShouldReturn409_WhenDepartmentWithNameAlreadyExists() throws JSONException {
            String authHeader = testAuthenticator.authenticateAsAdmin();
            String requestBody = prepareRequestBody("name", "Oddział psychologii №1");
            RequestEntity<String> request = RequestEntity
                    .post("/departments")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertNotNull(response.getBody());
            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        @Test
        public void createDepartment_ShouldReturn409_WhenDepartmentWithAddressAlreadyExists() throws JSONException {
            String authHeader = testAuthenticator.authenticateAsAdmin();
            String requestBody = prepareRequestBody(
                    "address",
                    new JSONObject("""
                    {
                        "city": "Lublin",
                        "street": "Lubartowska",
                        "buildingNumber": "47",
                        "flatNumber": null,
                        "zipCode": "20-890"
                    }""")
            );
            RequestEntity<String> request = RequestEntity
                    .post("/departments")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody);

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertNotNull(response.getBody());
            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }

        private String prepareRequestBody(Map<String, String> properties) throws JSONException {
            JSONObject json = new JSONObject(validRequestBody());
            properties.forEach((key, value) -> JSONUtil.setProperty(json, key, value));
            return json.toString();
        }

        private String prepareRequestBody(String property, String value) throws JSONException {
            JSONObject json = new JSONObject(validRequestBody());
            JSONUtil.setProperty(json, property, value);
            return json.toString();
        }

        private String prepareRequestBody(String property, JSONObject value) throws JSONException {
            JSONObject json = new JSONObject(validRequestBody());
            JSONUtil.setProperty(json, property, value);
            return json.toString();
        }

        private String validRequestBody() {
            try(InputStream is = getClass().getResourceAsStream(JSON_PATH)){
                return new String(is.readAllBytes());
            } catch(IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }

    @Nested
    class DeleteDepartment {
        @Test
        public void deleteDepartment_Basic(){
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsAdmin());
            HttpEntity<Void> request = new HttpEntity<>(headers);
            Long departmentId = 2L;

            ResponseEntity<Void> response = restTemplate.exchange(
                    "/departments/{id}", HttpMethod.DELETE, request, Void.class, departmentId
            );

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
        }

        @Test
        public void deleteDepartment_ShouldReturn404_WhenDepartmentNotFound(){
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsAdmin());
            HttpEntity<Void> request = new HttpEntity<>(headers);
            Long departmentId = 111L;

            ResponseEntity<String> response = restTemplate.exchange(
                    "/departments/{id}", HttpMethod.DELETE, request, String.class, departmentId
            );
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(responseJson.read("$.status"), is(HttpStatus.NOT_FOUND.name()));
        }

        @Test
        public void deleteDepartment_ShouldReturn403_WhenAuthHeaderIsNotPresent(){
            Long departmentId = 2L;

            ResponseEntity<String> response = restTemplate.exchange(
                    "/departments/{id}", HttpMethod.DELETE, HttpEntity.EMPTY, String.class, departmentId
            );
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
            assertThat(responseJson.read("$.status"), is(HttpStatus.FORBIDDEN.name()));
            assertThat(responseJson.read("$.message"), is("Authorization header is invalid or is not present"));
        }

        @ParameterizedTest
        @EnumSource(value = Role.class, names = {"PATIENT", "HEAD_OF_DEPARTMENT", "DOCTOR"})
        public void deleteDepartment_ShouldReturn403_WhenRoleIsNotAdmin(Role role){
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAs(role));
            HttpEntity<Void> request = new HttpEntity<>(headers);
            Long departmentId = 2L;

            ResponseEntity<Void> response = restTemplate.exchange(
                    "/departments/{id}", HttpMethod.DELETE, request, Void.class, departmentId
            );

            assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
        }

        @Test
        public void deleteDepartment_ShouldReturn409_WhenDepartmentHasDoctors(){
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, testAuthenticator.authenticateAsAdmin());
            HttpEntity<Void> request = new HttpEntity<>(headers);
            Long departmentId = 1L;

            ResponseEntity<String> response = restTemplate.exchange(
                    "/departments/{id}", HttpMethod.DELETE, request, String.class, departmentId
            );
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));
            assertThat(responseJson.read("$.status"), is(HttpStatus.CONFLICT.name()));
        }
    }
}
