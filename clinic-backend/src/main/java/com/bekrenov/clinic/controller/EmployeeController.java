package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.EmployeeRequest;
import com.bekrenov.clinic.dto.response.EmployeeDetailedResponse;
import com.bekrenov.clinic.dto.response.EmployeeResponse;
import com.bekrenov.clinic.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    @Secured({"HEAD_OF_DEPARTMENT", "ADMIN"})
    public ResponseEntity<List<EmployeeResponse>> getAllActiveEmployees() {
        return ResponseEntity.ok(employeeService.getAllActiveEmployees());
    }

    @GetMapping("/dismissed")
    @Secured({"HEAD_OF_DEPARTMENT", "ADMIN"})
    public ResponseEntity<List<EmployeeResponse>> getAllDismissedEmployees() {
        return ResponseEntity.ok(employeeService.getAllDismissedEmployees());
    }

    @PostMapping
    @Secured({"HEAD_OF_DEPARTMENT", "ADMIN"})
    public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(request));
    }

    @DeleteMapping("/dismiss/{id}")
    @Secured({"HEAD_OF_DEPARTMENT", "ADMIN"})
    public void dismissEmployee(@PathVariable Long id) {
        employeeService.dismissEmployee(id);
    }
}
