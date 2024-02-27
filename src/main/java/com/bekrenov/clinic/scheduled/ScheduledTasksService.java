package com.bekrenov.clinic.scheduled;

import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.bekrenov.clinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class ScheduledTasksService {
    private final AppointmentRepository appointmentRepository;

    @Value("${business.schedule.visit-duration-mins}")
    private int visitDurationMinutes;

    @Scheduled(fixedRate = 14400000)
    public void finishAllPastAppointments(){
        Predicate<Appointment> isEnded = appointment -> {
            LocalDateTime endDateTime = LocalDateTime.of(appointment.getDate(), appointment.getTime()).plusMinutes(visitDurationMinutes);
            return LocalDateTime.now().isAfter(endDateTime);
        };
        List<Appointment> confirmedAppointmentsInPast = appointmentRepository.findAllWithStatusConfirmed()
                .stream()
                .filter(isEnded)
                .toList();
        confirmedAppointmentsInPast.forEach(a -> a.setStatus(AppointmentStatus.FINISHED));
        appointmentRepository.saveAll(confirmedAppointmentsInPast);
    }
}
