package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.service.HolidaysService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<Void> addHolidays(@RequestParam List<LocalDate> dates){
        holidaysService.addHolidays(dates);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping
    @Secured("ADMIN")
    public ResponseEntity<Void> removeHolidays(@RequestParam List<LocalDate> dates){
        holidaysService.removeHolidays(dates);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
