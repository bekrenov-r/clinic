package com.bekrenov.clinic.service;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Department;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DepartmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.DEPARTMENT;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    private final List<LocalTime> allPossibleTimes;

    @Autowired
    public AppointmentService(AppointmentRepository repository, DoctorRepository doctorRepository, DepartmentRepository departmentRepository) {
        this.appointmentRepository = repository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        // todo: put lunch break values in properties file
        allPossibleTimes = setAllPossibleTimes();
    }

    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public Appointment findById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);
    }

    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<LocalTime> getAvailableTimesByDepartment(Long departmentId, LocalDate date) {
        // get list of doctors from db
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ClinicEntityNotFoundException(DEPARTMENT, departmentId));
        List<Doctor> doctors = doctorRepository.findByDepartment(department);

        // create set with free times of every doctor in list
        Set<LocalTime> times = new TreeSet<>();
        for(Doctor doctor : doctors){
            List<LocalTime> timesForSingleDoctor = getAvailableTimesByDoctor(doctor.getId(), date);
            times.addAll(timesForSingleDoctor);
        }
        return new ArrayList<>(times);
    }

    public List<LocalTime> getAvailableTimesByDoctor(Long doctorId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor_IdAndDate(doctorId, date);
        List<LocalTime> timesReserved = new ArrayList<>();
        for(Appointment appointment : appointments){
            timesReserved.add(appointment.getTime());
        }

        return (List<LocalTime>) CollectionUtils.subtract(allPossibleTimes, timesReserved);
    }

    /**
     * Returns a list of all possible times to make an appointment (e.g. 08:00, 08:15, 08:30 etc)
     */
    private List<LocalTime> setAllPossibleTimes(){
        int visitDurationSeconds = DoctorService.VISIT_DURATION_MINUTES*60;

        int workingDayBeginTimeSeconds = DoctorService.WORKING_DAY_BEGIN_TIME.toSecondOfDay();
        int workingDayEndTimeSeconds = DoctorService.WORKING_DAY_END_TIME.toSecondOfDay();

        List<LocalTime> result = new ArrayList<>();
        for(int i=workingDayBeginTimeSeconds; i<workingDayEndTimeSeconds; i+=visitDurationSeconds){
            if(i >= DoctorService.LUNCH_BREAK_BEGIN.toSecondOfDay() && i < DoctorService.LUNCH_BREAK_END.toSecondOfDay()){
                continue;
            }
            result.add(LocalTime.ofSecondOfDay(i));
        }
        return result;
    }

}
