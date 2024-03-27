package com.bekrenov.clinic.service;

import com.bekrenov.clinic.repository.HolidaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidaysService {
    private final HolidaysRepository holidaysRepository;

    public List<LocalDate> getAllHolidays(){
        return holidaysRepository.getAllHolidays();
    }
}
