package com.bekrenov.clinic.rest_controller;

import com.bekrenov.clinic.entity.Department;
import com.bekrenov.clinic.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentRestController {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentRestController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/{specialization}")
    public List<Department> getDepartmentsBySpecialization(@PathVariable String specialization){
        return departmentRepository.findDepartmentsBySpecialization(specialization);
    }

}
