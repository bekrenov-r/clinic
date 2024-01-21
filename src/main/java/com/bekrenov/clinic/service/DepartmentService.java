package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.DepartmentMapper;
import com.bekrenov.clinic.dto.request.DepartmentRequest;
import com.bekrenov.clinic.dto.response.DepartmentResponse;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.enums.AppointmentStatus;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.CANNOT_DELETE_DEPARTMENT_WITH_APPOINTMENTS;
import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.CANNOT_DELETE_DEPARTMENT_WITH_DOCTORS;
import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final AppointmentRepository appointmentRepository;

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::entityToResponse)
                .toList();
    }

    public List<DepartmentResponse> getAllDepartmentsBySpecialization(Department.Specialization specialization){
        return departmentRepository.findAllBySpecialization(specialization).stream()
                .map(departmentMapper::entityToResponse)
                .toList();
    }

    public DepartmentResponse createDepartment(DepartmentRequest request) {
        Department department = departmentMapper.requestToEntity(request);
        return departmentMapper.entityToResponse(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, id));
        assertDepartmentHasNoDoctors(department);
        assertDepartmentHasNoActiveAppointments(department);
        departmentRepository.delete(department);
    }

    private void assertDepartmentHasNoDoctors(Department department) {
        if(!department.getDoctors().isEmpty())
            throw new ClinicApplicationException(CANNOT_DELETE_DEPARTMENT_WITH_DOCTORS, department.getId());
    }

    private void assertDepartmentHasNoActiveAppointments(Department department) {
        Predicate<Appointment> isActive = a ->
                a.getStatus().equals(AppointmentStatus.PENDING) || a.getStatus().equals(AppointmentStatus.CONFIRMED);

        boolean hasActiveAppointments = appointmentRepository.findAllByDepartment(department)
                .stream()
                .anyMatch(isActive);
        if(hasActiveAppointments)
            throw new ClinicApplicationException(CANNOT_DELETE_DEPARTMENT_WITH_APPOINTMENTS);
    }


}
