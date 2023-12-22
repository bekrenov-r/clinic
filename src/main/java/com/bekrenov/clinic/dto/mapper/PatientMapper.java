package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.PatientRegistrationRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public abstract class PatientMapper {
    public abstract Patient requestToEntity(PatientRegistrationRequest request);

    public abstract PatientResponse entityToResponse(Patient patient);
}
