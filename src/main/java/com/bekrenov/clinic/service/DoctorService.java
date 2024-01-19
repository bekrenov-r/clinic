package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DoctorMapper;
import com.bekrenov.clinic.dto.response.PersonDto;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Specialization;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.SPECIALIZATION;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final AppointmentService appointmentService;
    private final SpecializationRepository specializationRepository;

    // duration for each visit in minutes
    public static final int VISIT_DURATION_MINUTES = 15;

    // time for beginning and ending of working day (by default 08:00-16:00)
    public static final LocalTime WORKING_DAY_BEGIN_TIME = LocalTime.of(8, 0);
    public static final LocalTime WORKING_DAY_END_TIME = LocalTime.of(16, 0);

    // time for beginning and ending of lunch break (by default 13:00-13:30)
    public static final LocalTime LUNCH_BREAK_BEGIN = LocalTime.of(13,0);
    public static final LocalTime LUNCH_BREAK_END = LocalTime.of(13, 30);

    public List<? extends PersonDto> getDoctorsByDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
        return mapToResponseDependingOnRole(doctorRepository.findByDepartment(department));
    }

    public List<? extends PersonDto> getDoctorsBySpecialization(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(SPECIALIZATION, id));
        return mapToResponseDependingOnRole(doctorRepository.findBySpecialization(specialization));
    }

    private List<? extends PersonDto> mapToResponseDependingOnRole(List<Doctor> doctors){
        // todo: get role of current user, return DoctorResponse or DoctorShortResponse accordingly
        return doctors.stream()
                .map(doctorMapper::entityToShortResponse)
                .toList();
    }

    public Doctor getAnyDoctorForAppointment(Appointment appointment) {
        List<Doctor> doctors = doctorRepository.findByDepartment(appointment.getDepartment());
        for (Doctor doctor : doctors) {
            List<LocalTime> times = appointmentService.getAvailableTimesByDoctor(doctor.getId(), appointment.getDate());
            if (times.contains(appointment.getTime())) {
                return doctor;
            }
        }
        return null;
    }
}
