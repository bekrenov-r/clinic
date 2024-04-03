package com.bekrenov.clinic.validation.constraint;

import com.bekrenov.clinic.repository.HolidaysRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkingDayValidator implements ConstraintValidator<WorkingDay, LocalDate> {
    private final HolidaysRepository holidaysRepository;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        List<LocalDate> holidays = holidaysRepository.getAllHolidays();
        return isNotOnWeekend(date) && !holidays.contains(date);
    }

    private boolean isNotOnWeekend(LocalDate date){
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return !dayOfWeek.equals(DayOfWeek.SATURDAY)
                && !dayOfWeek.equals(DayOfWeek.SUNDAY);
    }
}
