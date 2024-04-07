package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.AppointmentMapper;
import com.bekrenov.clinic.dto.request.AppointmentRequestByDoctor;
import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import com.bekrenov.clinic.dto.response.AppointmentResponse;
import com.bekrenov.clinic.dto.response.AppointmentShortResponse;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason;
import com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason;
import com.bekrenov.clinic.model.entity.*;
import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.security.Role;
import com.bekrenov.clinic.util.*;
import com.bekrenov.clinic.validation.AppointmentAssert;
import com.bekrenov.clinic.validation.DoctorAssert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.NO_AVAILABLE_DOCTORS_IN_DEPARTMENT;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.PATIENT_BY_PESEL;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final PatientRepository patientRepository;
    private final PatientService patientService;
    private final AppointmentMapper appointmentMapper;
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

    public AppointmentResponse getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findByIdOrThrowDefault(id);
        Person currentPerson = CurrentAuthUtil.hasAuthority(Role.DOCTOR)
                ? doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName())
                : patientRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        AppointmentAssert.assertPersonIsAppointmentOwner(appointment, currentPerson);
        return appointmentMapper.entityToResponse(appointment);
    }

    public AppointmentResponse createAppointmentAsDoctor(AppointmentRequestByDoctor request) {
        Appointment appointment = appointmentMapper.requestByDoctorToEntity(request);
        Doctor doctor = doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        availabilityService.validateAvailabilityByDoctor(doctor, request.date(), request.time());
        Patient patient = patientRepository.findByIdOrThrowDefault(request.patientId());
        AppointmentAssert.assertPatientHasNoAppointmentAtDateTime(patient, request.date(), request.time());

        appointment.setDoctor(doctor);
        appointment.setDepartment(doctor.getDepartment());
        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        mailService.sendEmailWithAppointment(appointment);
        return appointmentMapper.entityToResponse(appointmentRepository.save(appointment));
    }

    public AppointmentResponse createAppointmentAsPatient(AppointmentRequestByPatient request) {
        Appointment appointment = appointmentMapper.requestByPatientToEntity(request);
        Department department = departmentRepository.findByIdOrThrowDefault(request.departmentId());
        Doctor doctor = resolveDoctorFromRequest(request, department);
        Patient patient = resolvePatientFromRequest(request);
        AppointmentAssert.assertPatientHasNoAppointmentAtDateTime(patient, request.date(), request.time());
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
        Appointment appointment = appointmentRepository.findByIdOrThrowDefault(id);
        Doctor doctor = doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        AppointmentAssert.assertDoctorIsAppointmentOwner(appointment, doctor);
        AppointmentAssert.assertAppointmentCanBeConfirmed(appointment);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
        mailService.sendEmailWithAppointment(appointment);
    }

    public void finishAppointment(Long id) {
        Appointment appointment = appointmentRepository.findByIdOrThrowDefault(id);
        Doctor doctor = doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        AppointmentAssert.assertDoctorIsAppointmentOwner(appointment, doctor);
        AppointmentAssert.assertAppointmentCanBeFinished(appointment);
        appointment.setStatus(AppointmentStatus.FINISHED);
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findByIdOrThrowDefault(id);
        Patient patient = patientRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        AppointmentAssert.assertPatientIsAppointmentOwner(appointment, patient);
        AppointmentAssert.assertAppointmentCanBeCancelled(appointment);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        mailService.sendEmailWithAppointment(appointment);
    }

    private List<Appointment> getAllAppointmentsDependingOnRole(){
        String username = CurrentAuthUtil.getAuthentication().getName();
        if(CurrentAuthUtil.hasAuthority(Role.DOCTOR)){
            Doctor doctor = doctorRepository.findByEmail(username);
            return appointmentRepository.findAllByDoctor(doctor);
        } else {
            Patient patient = patientRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
            return appointmentRepository.findAllByPatient(patient);
        }
    }

    private Doctor resolveDoctorFromRequest(AppointmentRequestByPatient request, Department department){
        if(request.anyDoctor()){
            return findAnyDoctorForAppointment(department, request.date(), request.time());
        } else {
            Doctor doctor = doctorRepository.findByIdOrThrowDefault(request.doctorId());
            DoctorAssert.assertDoctorIsFromDepartment(doctor, department);
            availabilityService.validateAvailabilityByDoctor(doctor, request.date(), request.time());
            return doctor;
        }
    }

    private Patient resolvePatientFromRequest(AppointmentRequestByPatient request) {
        if(CurrentAuthUtil.isAuthenticated()){
            return patientRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        } else {
            String pesel = request.patient().pesel();
            if(patientRepository.existsByPesel(pesel)){
                return patientService.updatePatient(
                        patientRepository.findByPesel(pesel)
                                .orElseThrow(() -> new ClinicEntityNotFoundException(PATIENT_BY_PESEL))
                                .getId(),
                        request.patient()
                );
            } else {
                return patientService.createPatient(request.patient());
            }
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
}
