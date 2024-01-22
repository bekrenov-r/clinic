package com.bekrenov.clinic.service;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DOCTOR;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Value("${business.schedule.visit-duration-mins}")
    private int visitDurationMinutes;

    @Value("${business.schedule.working-day.begins}")
    private String workingDayBeginTime;

    @Value("${business.schedule.working-day.duration-hours}")
    private int workingDayDurationHours;

    @Value("${business.schedule.break.begins}")
    private String breakBegins;

    @Value("${business.schedule.break.duration-mins}")
    private int breakDurationMinutes;
    private Set<LocalTime> allTimes;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mma");

    public Set<LocalTime> getAvailableTimesByDepartment(Long departmentId, LocalDate date) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, departmentId));
        List<Doctor> doctors = doctorRepository.findByDepartment(department);

        return doctors.stream()
                .map(d -> getAvailableTimesByDoctor(d.getId(), date))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<LocalTime> getAvailableTimesByDoctor(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DOCTOR, doctorId));
        Set<LocalTime> reservedTimes = appointmentRepository.findAllByDoctorAndDate(doctor, date)
                .stream()
                .map(Appointment::getTime)
                .collect(Collectors.toSet());
        return allTimes.stream()
                .filter(t -> !reservedTimes.contains(t))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Generates a list of all possible times to make an appointment (e.g. 08:00, 08:15, 08:30 etc)
     */
    private Set<LocalTime> generateAllPossibleTimes(){
        int visitDurationSeconds = visitDurationMinutes * 60;
        int workingDayBeginTimeSeconds = LocalTime.parse(workingDayBeginTime, FORMATTER).toSecondOfDay();
        int workingDayEndTimeSeconds = LocalTime.parse(workingDayBeginTime, FORMATTER).plusHours(workingDayDurationHours).toSecondOfDay();
        int lunchBreakBeginTimeSeconds = LocalTime.parse(breakBegins, FORMATTER).toSecondOfDay();
        int lunchBreakEndTimeSeconds = LocalTime.parse(breakBegins, FORMATTER).plusMinutes(breakDurationMinutes).toSecondOfDay();

        return Stream.iterate(workingDayBeginTimeSeconds, i -> i < workingDayEndTimeSeconds, i -> i + visitDurationSeconds)
                .filter(i -> i < lunchBreakBeginTimeSeconds || i >= lunchBreakEndTimeSeconds)
                .map(LocalTime::ofSecondOfDay)
                .collect(Collectors.toSet());
    }

    @PostConstruct
    private void postConstruct(){
        allTimes = generateAllPossibleTimes();
    }
}
