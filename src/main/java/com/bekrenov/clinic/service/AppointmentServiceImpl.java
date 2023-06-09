package com.bekrenov.clinic.service;

import com.bekrenov.clinic.entity.Appointment;
import com.bekrenov.clinic.entity.Doctor;
import com.bekrenov.clinic.repository.AppointmentRepository;
import com.bekrenov.clinic.repository.DoctorRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    private final List<LocalTime> allPossibleTimes;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository repository, DoctorRepository doctorRepository) {
        this.appointmentRepository = repository;
        this.doctorRepository = doctorRepository;
        // todo: put lunch break values in properties file
        allPossibleTimes = setAllPossibleTimes();
    }

    @Override
    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment findById(int id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        return appointment.orElse(null);
    }

    @Override
    public void deleteById(int id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<LocalTime> getAvailableTimesByDepartment(int departmentId, LocalDate date) {
        // get list of doctors from db
        List<Doctor> doctors = doctorRepository.findDoctorsByDepartment_Id(departmentId);

        // create set with free times of every doctor in list
        Set<LocalTime> times = new TreeSet<>();
        for(Doctor doctor : doctors){
            List<LocalTime> timesForSingleDoctor = getAvailableTimesByDoctor(doctor.getId(), date);
            times.addAll(timesForSingleDoctor);
        }
        return new ArrayList<>(times);
    }

    @Override
    public List<LocalTime> getAvailableTimesByDoctor(int doctorId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor_IdAndAppointmentDate(doctorId, date);
        List<LocalTime> timesReserved = new ArrayList<>();
        for(Appointment appointment : appointments){
            timesReserved.add(appointment.getAppointmentTime());
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
