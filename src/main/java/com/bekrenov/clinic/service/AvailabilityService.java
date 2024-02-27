package com.bekrenov.clinic.service;

import com.bekrenov.clinic.exception.ClinicApplicationException;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.APPOINTMENT_TIME_IS_NOT_AVAILABLE;

@Service
@RequiredArgsConstructor
public class AvailabilityService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Value("${business.schedule.visit-duration-mins}")
    private int appointmentDurationMinutes;

    @Value("#{T(java.time.LocalTime).parse('${business.schedule.working-day.begins}', T(java.time.format.DateTimeFormatter).ofPattern('HH:mm'))}")
    private LocalTime workingDayBeginTime;

    @Value("${business.schedule.working-day.duration-hours}")
    private int workingDayDurationHours;

    @Value("#{T(java.time.LocalTime).parse('${business.schedule.break.begins}', T(java.time.format.DateTimeFormatter).ofPattern('HH:mm'))}")
    private LocalTime breakBeginTime;

    @Value("${business.schedule.break.duration-mins}")
    private int breakDurationMinutes;
    private Set<LocalTime> allTimes;

    public Set<LocalTime> getAvailableTimesByDepartment(Long departmentId, LocalDate date) {
        Department department = departmentRepository.findByIdOrThrowDefault(departmentId);
        List<Doctor> doctors = doctorRepository.findByDepartment(department);

        return doctors.stream()
                .map(d -> getAvailableTimesByDoctor(d.getId(), date))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public Set<LocalTime> getAvailableTimesByDoctor(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findByIdOrThrowDefault(doctorId);
        return getAvailableTimesByDoctor(doctor, date);
    }

    public Set<LocalTime> getAvailableTimesByDoctor(Doctor doctor, LocalDate date){
        Set<LocalTime> reservedTimes = appointmentRepository.findAllByDoctorAndDate(doctor, date)
                .stream()
                .map(Appointment::getTime)
                .collect(Collectors.toSet());
        return allTimes.stream()
                .filter(t -> !reservedTimes.contains(t))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void validateAvailabilityByDoctor(Doctor doctor, LocalDate date, LocalTime time){
        boolean appointmentTimeIsAvailable = getAvailableTimesByDoctor(doctor.getId(), date).contains(time);
        if(!appointmentTimeIsAvailable)
            throw new ClinicApplicationException(APPOINTMENT_TIME_IS_NOT_AVAILABLE, time, date);
    }

    /**
     * Generates a list of all possible times to make an appointment (e.g. 08:00, 08:15, 08:30 etc)
     */
    private Set<LocalTime> generateAllPossibleTimes(){
        int visitDurationSeconds = appointmentDurationMinutes * 60;
        int workingDayBeginTimeSeconds = workingDayBeginTime.toSecondOfDay();
        int workingDayEndTimeSeconds = workingDayBeginTime.plusHours(workingDayDurationHours).toSecondOfDay();
        int lunchBreakBeginTimeSeconds = breakBeginTime.toSecondOfDay();
        int lunchBreakEndTimeSeconds = breakBeginTime.plusMinutes(breakDurationMinutes).toSecondOfDay();

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
