package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.PatientRegistrationRequest;
import com.bekrenov.clinic.dto.response.PatientResponse;
import com.bekrenov.clinic.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public abstract class PatientMapper {
    @Mapping(source = "pesel", target = "gender", qualifiedByName = "determineGender")
    public abstract Patient requestToEntity(PatientRegistrationRequest request);

    public abstract PatientResponse entityToResponse(Patient patient);

    @Named("determineGender")
    protected Patient.Gender determineGender(String pesel){
        if((int) pesel.charAt(9) % 2 == 0)
            return Patient.Gender.FEMALE;
        else
            return Patient.Gender.MALE;
    }
}
