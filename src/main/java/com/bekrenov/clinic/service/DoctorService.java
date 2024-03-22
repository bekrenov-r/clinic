package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DoctorMapper;
import com.bekrenov.clinic.dto.response.DoctorDetailedResponse;
import com.bekrenov.clinic.dto.response.PersonDTO;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.security.Role;
import com.bekrenov.clinic.util.CurrentAuthUtil;
import com.bekrenov.clinic.validation.DoctorAssert;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.NOT_ENTITY_OWNER;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorDetailedResponse getDoctorProfile() {
        Doctor currentDoctor = doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
        return doctorMapper.entityToDetailedResponse(currentDoctor);
    }

    public DoctorDetailedResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findByIdOrThrowDefault(id);

        if(!CurrentAuthUtil.hasAuthority(Role.ADMIN)){
            Doctor headOfDepartment = doctorRepository.findByEmail(CurrentAuthUtil.getAuthentication().getName());
            DoctorAssert.assertDoctorIsFromDepartment(doctor, headOfDepartment.getDepartment());
        }

        return doctorMapper.entityToDetailedResponse(doctor);
    }

    public List<PersonDTO> getDoctorsByDepartment(Long id, HttpServletRequest request) {
        Department department = departmentRepository.findByIdOrThrowDefault(id);
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
}
