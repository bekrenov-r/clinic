package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.AppointmentMapper;
import com.bekrenov.clinic.dto.request.AppointmentRequestByDoctor;
import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import com.bekrenov.clinic.dto.response.AppointmentResponse;
import com.bekrenov.clinic.dto.response.AppointmentShortResponse;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.security.Role;
import com.bekrenov.clinic.util.AppointmentSortComparator;
import com.bekrenov.clinic.util.CurrentUserUtil;
import com.bekrenov.clinic.util.MailService;
import com.bekrenov.clinic.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.*;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final CurrentUserUtil currentUserUtil;
    private final AvailabilityService availabilityService;
    private final MailService mailService;

    @Value("${business.data.page-size}")
    private Integer pageSize;

    public Page<AppointmentShortResponse> getAllAppointmentsForCurrentUser(
            Integer page, AppointmentStatus status
    ) {
        List<AppointmentShortResponse> appointments  = getAllAppointmentsDependingOnRole().stream()
                .filter(a -> status == null || a.getStatus().equals(status))
                .sorted(new AppointmentSortComparator())
                .map(appointmentMapper::entityToShortResponse)
                .toList();
        return PageUtil.paginateList(appointments, page, pageSize);
    }

    public AppointmentResponse createAppointmentAsDoctor(AppointmentRequestByDoctor request) {
        Appointment appointment = appointmentMapper.requestByDoctorToEntity(request);
        Doctor doctor = doctorRepository.findByEmail(currentUserUtil.getCurrentUser().getUsername());
        availabilityService.validateAvailabilityByDoctor(doctor, request.date(), request.time());
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ClinicEntityNotFoundException(PATIENT, request.patientId()));
        assertPatientHasNoAppointmentAtDateTime(patient, request.date(), request.time());

        appointment.setDoctor(doctor);
        appointment.setDepartment(doctor.getDepartment());
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        mailService.sendEmailWithAppointment(appointment);
        return appointmentMapper.entityToResponse(appointmentRepository.save(appointment));
    }

    public AppointmentResponse createAppointmentAsPatient(AppointmentRequestByPatient request) {
        Appointment appointment = appointmentMapper.requestByPatientToEntity(request);
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, request.departmentId()));
        Doctor doctor = resolveDoctorFromRequest(request, department);
        Patient patient = patientRepository.findByEmail(currentUserUtil.getCurrentUser().getUsername());
        assertPatientHasNoAppointmentAtDateTime(patient, request.date(), request.time());
        AppointmentStatus status = department.getAutoConfirmAppointment()
                ? AppointmentStatus.CONFIRMED
                : AppointmentStatus.PENDING;

        appointment.setDepartment(department);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(status);
        mailService.sendEmailWithAppointment(appointment);
        return appointmentMapper.entityToResponse(appointmentRepository.save(appointment));
    }

    public void confirmAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(APPOINTMENT, id));
        assertAppointmentCanBeConfirmed(appointment);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
        mailService.sendEmailWithAppointment(appointment);
    }

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(APPOINTMENT, id));
        assertPatientIsAppointmentOwner(appointment);
        assertAppointmentCanBeCancelled(appointment);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        mailService.sendEmailWithAppointment(appointment);
    }

    private List<Appointment> getAllAppointmentsDependingOnRole(){
        String username = currentUserUtil.getCurrentUser().getUsername();
        if(currentUserUtil.getCurrentUserRoles().contains(Role.DOCTOR)){
            Doctor doctor = doctorRepository.findByEmail(username);
            return appointmentRepository.findAllByDoctor(doctor);
        } else {
            Patient patient = patientRepository.findByEmail(currentUserUtil.getCurrentUser().getUsername());
            return appointmentRepository.findAllByPatient(patient);
        }
    }

    private Doctor resolveDoctorFromRequest(AppointmentRequestByPatient request, Department department){
        if(request.anyDoctor()){
            return findAnyDoctorForAppointment(department, request.date(), request.time());
        } else {
            Doctor doctor = doctorRepository.findById(request.doctorId())
                    .orElseThrow(() -> new ClinicEntityNotFoundException(DOCTOR, request.doctorId()));
            assertDoctorIsFromDepartment(doctor, department);
            availabilityService.validateAvailabilityByDoctor(doctor, request.date(), request.time());
            return doctor;
        }
    }

    private Doctor findAnyDoctorForAppointment(Department department, LocalDate date, LocalTime time) {
        List<Doctor> doctors = doctorRepository.findByDepartment(department);
        Predicate<Doctor> doctorIsAvailable =
                doctor -> availabilityService.getAvailableTimesByDoctor(doctor, date).contains(time);
        return doctors.stream()
                .filter(doctorIsAvailable)
                .findAny()
                .orElseThrow(() -> new ClinicApplicationException(NO_AVAILABLE_DOCTORS_IN_DEPARTMENT));
    }

    private void assertDoctorIsFromDepartment(Doctor doctor, Department department){
        if(!doctor.getDepartment().equals(department))
            throw new ClinicApplicationException(DOCTOR_IS_NOT_FROM_DEPARTMENT, doctor.getId());
    }

    private void assertPatientHasNoAppointmentAtDateTime(Patient patient, LocalDate date, LocalTime time) {
        if(appointmentRepository.existsByPatientAndDateAndTime(patient, date, time))
            throw new ClinicApplicationException(PATIENT_ALREADY_HAS_APPOINTMENT_AT_DATETIME, date, time);
    }

    private void assertPatientIsAppointmentOwner(Appointment appointment) {
        Patient patient = patientRepository.findByEmail(currentUserUtil.getCurrentUser().getUsername());
        if(!appointment.getPatient().equals(patient))
            throw new ClinicApplicationException(NOT_ENTITY_OWNER);
    }

    private void assertAppointmentCanBeCancelled(Appointment appointment) {
        AppointmentStatus status = appointment.getStatus();
        if(status.equals(AppointmentStatus.CANCELLED) || status.equals(AppointmentStatus.FINISHED))
            throw new ClinicApplicationException(CANNOT_CANCEL_APPOINTMENT);
    }

    private void assertAppointmentCanBeConfirmed(Appointment appointment) {
        if(!appointment.getStatus().equals(AppointmentStatus.PENDING))
            throw new ClinicApplicationException(CANNOT_CONFIRM_APPOINTMENT);
    }
}
