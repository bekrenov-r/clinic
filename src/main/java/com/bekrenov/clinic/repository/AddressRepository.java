package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.exception.ClinicEntityNotFoundException;
import com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason;
import com.bekrenov.clinic.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.bekrenov.clinic.exception.reason.ClinicEntityNotFoundExceptionReason.ADDRESS;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    default Address findByIdOrThrowDefault(Long id){
        return findById(id)
                .orElseThrow(() -> new ClinicEntityNotFoundException(ADDRESS));
    }
}
