package com.bekrenov.clinic.integrationtests;

import com.bekrenov.clinic.integrationtests.util.JSONUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AvailabilityIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class GetAvailableTimesByDepartment {
        private static final String URI_TEMPLATE =
                "/appointments/availability/department?departmentId={id}&date={date}";
        @Test
        public void getAvailableTimesByDepartment_Basic(){
            Long departmentId = 1L;
            String date = LocalDate.now().plusDays(1).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, departmentId, date);

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
            assertTrue(JSONUtil.isJsonArray(response.getBody()));
        }

        @Test
        public void getAvailableTimesByDepartment_DepartmentNotFound(){
            Long departmentId = 111L;
            String date = LocalDate.now().plusDays(1).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, departmentId, date);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(responseJson.read("$.status"), is(HttpStatus.NOT_FOUND.name()));
        }

        @Test
        public void getAvailableTimesByDepartment_DateIsInThePast(){
            Long departmentId = 1L;
            String date = LocalDate.now().minusDays(1).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, departmentId, date);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }

        @Test
        public void getAvailableTimesByDepartment_DateIsHoliday(){
            Long departmentId = 1L;
            String christmasDate = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 25).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, departmentId, christmasDate);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }
    }

    @Nested
    class GetAvailableTimesByDoctor {
        private static final String URI_TEMPLATE =
                "/appointments/availability/doctor?doctorId={id}&date={date}";
        @Test
        public void getAvailableTimesByDoctor_Basic(){
            Long doctorId = 1L;
            String date = LocalDate.now().plusDays(1).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, doctorId, date);

            assertThat(response.getStatusCode(), is(HttpStatus.OK));
            assertTrue(JSONUtil.isJsonArray(response.getBody()));
        }

        @Test
        public void getAvailableTimesByDoctor_DoctorNotFound(){
            Long doctorId = 111L;
            String date = LocalDate.now().plusDays(1).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, doctorId, date);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
            assertThat(responseJson.read("$.status"), is(HttpStatus.NOT_FOUND.name()));
        }

        @Test
        public void getAvailableTimesByDoctor_DateIsInThePast(){
            Long doctorId = 1L;
            String date = LocalDate.now().minusDays(1).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, doctorId, date);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }

        @Test
        public void getAvailableTimesByDoctor_DateIsHoliday(){
            Long doctorId = 1L;
            String christmasDate = LocalDate.of(LocalDate.now().getYear(), Month.DECEMBER, 25).toString();
            ResponseEntity<String> response = restTemplate.getForEntity(URI_TEMPLATE, String.class, doctorId, christmasDate);
            DocumentContext responseJson = JsonPath.parse(response.getBody());

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
            assertThat(responseJson.read("$.status"), is(HttpStatus.BAD_REQUEST.name()));
        }
    }
}