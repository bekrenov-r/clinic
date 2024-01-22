package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DoctorMapper;
import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.DoctorShortResponse;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.enums.Role;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.util.CurrentUserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.NOT_ENTITY_OWNER;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DOCTOR;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final AvailabilityService availabilityService;
    private final CurrentUserUtil currentUserUtil;

    // duration for each visit in minutes
    public static final int VISIT_DURATION_MINUTES = 15;

    public DoctorDetailedResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DOCTOR, id));
        if(!currentUserUtil.getCurrentUserRoles().contains(Role.HEAD_OF_DEPARTMENT))
            assertCurrentUserIsAccountOwner(doctor);

        return doctorMapper.entityToDetailedResponse(doctor);
    }

    public List<DoctorShortResponse> getDoctorsByDepartment(Long id, HttpServletRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
        return doctorRepository.findByDepartment(department).stream()
                .map(doctorMapper::entityToShortResponse)
                .toList();
    }

    public List<DoctorShortResponse> getDoctorsBySpecialization(
            Department.Specialization specialization, HttpServletRequest request
    ) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(doctorMapper::entityToShortResponse)
                .toList();
    }

    private void assertCurrentUserIsAccountOwner(Doctor doctor) {
        UserDetails currentUser = currentUserUtil.getCurrentUser();
        if(!doctor.getEmail().equals(currentUser.getUsername()))
            throw new ClinicApplicationException(NOT_ENTITY_OWNER);
    }

    public Doctor getAnyDoctorForAppointment(Appointment appointment) {
        List<Doctor> doctors = doctorRepository.findByDepartment(appointment.getDepartment());
        for (Doctor doctor : doctors) {
            Set<LocalTime> times = availabilityService.getAvailableTimesByDoctor(doctor.getId(), appointment.getDate());
            if (times.contains(appointment.getTime())) {
                return doctor;
            }
        }
        return null;
    }
}
