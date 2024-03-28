package com.bekrenov.clinic.service;

import com.bekrenov.clinic.repository.HolidaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class HolidaysService {
    private final HolidaysRepository holidaysRepository;

    public List<LocalDate> getAllHolidays(){
        return holidaysRepository.getAllHolidays();
    }

    public void addHolidays(List<LocalDate> dates) {
        Predicate<LocalDate> isDuplicated = date -> getAllHolidays().contains(date);
        var filteredDates = dates.stream()
                .filter(not(isDuplicated))
                .toList();
        holidaysRepository.addHolidays(filteredDates);
    }

    public void removeHolidays(List<LocalDate> dates) {
        holidaysRepository.removeHolidays(dates);
    }
}
