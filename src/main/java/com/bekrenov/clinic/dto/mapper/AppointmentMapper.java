package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.AppointmentRequestByDoctor;
import com.bekrenov.clinic.dto.request.AppointmentRequestByPatient;
import com.bekrenov.clinic.dto.response.AppointmentResponse;
import com.bekrenov.clinic.model.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;

@Mapper(componentModel = "spring")
public abstract class AppointmentMapper {
    @Value("${business.schedule.visit-duration-mins}")
    protected int visitDurationMinutes;

    @Mapping(target = "startTime", source = "time")
    @Mapping(target = "endTime", source = "time", qualifiedByName = "calculateEndTime")
    public abstract AppointmentResponse entityToResponse(Appointment entity);

    public abstract Appointment requestByDoctorToEntity(AppointmentRequestByDoctor request);

    public abstract Appointment requestByPatientToEntity(AppointmentRequestByPatient request);

    @Named("calculateEndTime")
    protected LocalTime calculateEndTime(LocalTime time){
        return time.plusMinutes(visitDurationMinutes);
    }
}
