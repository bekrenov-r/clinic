package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DoctorMapper;
import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.PersonDTO;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.security.Role;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.NOT_ENTITY_OWNER;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DOCTOR;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorDetailedResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DOCTOR, id));
        if(!CurrentAuthUtil.hasAuthority(Role.HEAD_OF_DEPARTMENT))
            assertCurrentUserIsAccountOwner(doctor);

        return doctorMapper.entityToDetailedResponse(doctor);
    }

    public List<PersonDTO> getDoctorsByDepartment(Long id, HttpServletRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
        return doctorRepository.findByDepartment(department).stream()
                .map(doctorMapper::entityToPersonDto)
                .toList();
    }

    public List<PersonDTO> getDoctorsBySpecialization(
            Department.Specialization specialization, HttpServletRequest request
    ) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(doctorMapper::entityToPersonDto)
                .toList();
    }

    private void assertCurrentUserIsAccountOwner(Doctor doctor) {
        Authentication currentAuth = CurrentAuthUtil.getAuthentication();
        if(!doctor.getEmail().equals(currentAuth.getName()))
            throw new ClinicApplicationException(NOT_ENTITY_OWNER);
    }
}
