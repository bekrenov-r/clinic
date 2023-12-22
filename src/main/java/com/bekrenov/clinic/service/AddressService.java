package com.bekrenov.clinic.service;

import com.bekrenov.clinic.dto.mapper.AddressMapper;
import com.bekrenov.clinic.dto.request.AddressRequest;
import com.bekrenov.clinic.model.entity.Address;
import com.bekrenov.clinic.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Address createAddress(AddressRequest request){
        Address address = addressMapper.requestToEntity(request);
        return addressRepository.save(address);
    }
}
