package com.bekrenov.clinic.repository;

import com.bekrenov.clinic.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByEmail(String username);

    @Query("select case when count(p)>0 then true else false end " +
            "from Patient p where p.email = :email")
    boolean emailExists(String email);

    @Query("select case when count(p)>0 then true else false end " +
            "from Patient p where p.phoneNumber = :phoneNumber")
    boolean phoneNumberExists(String phoneNumber);

    @Query("select case when count(p)>0 then true else false end " +
            "from Patient p where p.pesel = :pesel")
    boolean peselExists(String pesel);
}
