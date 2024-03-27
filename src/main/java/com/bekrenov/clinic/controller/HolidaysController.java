package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.service.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class HolidaysController {
    private final HolidaysService holidaysService;

    @GetMapping
    public ResponseEntity<List<LocalDate>> getAllHolidays(){
        return ResponseEntity.ok(holidaysService.getAllHolidays());
    }
}
