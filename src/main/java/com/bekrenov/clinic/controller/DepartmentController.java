package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentRepository departmentRepository;

    @GetMapping("/{specialization}")
    public List<Department> getDepartmentsBySpecialization(@PathVariable String specialization){
        return departmentRepository.findDepartmentsBySpecialization(specialization);
    }

}
