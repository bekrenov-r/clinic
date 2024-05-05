package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.model.entity.Department;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/specializations")
public class SpecializationController {
    @GetMapping
    public ResponseEntity<Map<Department.Specialization, String>> getAllSpecializations(){
        Map<Department.Specialization, String> response = Arrays.stream(Department.Specialization.values())
                .collect(Collectors.toMap(s -> s, s -> s.getLangNameMap().get("en")));
        return ResponseEntity.ok(response);
    }
}
