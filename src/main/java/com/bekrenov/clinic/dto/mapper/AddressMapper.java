package com.bekrenov.clinic.dto.mapper;

import com.bekrenov.clinic.dto.request.AddressRequest;
import com.bekrenov.clinic.model.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {
    public abstract Address requestToEntity(AddressRequest addressRequest);
}
