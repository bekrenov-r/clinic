package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.dto.request.DepartmentRequest;
import com.bekrenov.clinic.dto.response.DepartmentResponse;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping(params = "spec")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments(@RequestParam("spec") Department.Specialization specialization){
        return ResponseEntity.ok(departmentService.getAllDepartmentsBySpecialization(specialization));
    }

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody @Valid DepartmentRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(request));
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok().build();
    }

}
