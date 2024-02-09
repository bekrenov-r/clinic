package com.bekrenov.clinic.validation;

import com.bekrenov.clinic.dto.request.RegistrationRequest;
import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.repository.DoctorRepository;
import com.bekrenov.clinic.repository.PatientRepository;
import com.bekrenov.clinic.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.*;

@Component
@RequiredArgsConstructor
public class RegistrationValidator {
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public void validateRegistrationRequest(RegistrationRequest request){
        assertEmailIsUnique(request.email());
        assertPhoneNumberIsUnique(request.phoneNumber());
        assertPeselIsUnique(request.pesel());
    }

    private void assertEmailIsUnique(String email){
        if(userService.existsByUsername(email))
            throw new ClinicApplicationException(ALREADY_REGISTERED_EMAIL, email);
    }

    private void assertPhoneNumberIsUnique(String phoneNumber){
        if(patientRepository.existsByPhoneNumber(phoneNumber) || doctorRepository.existsByPhoneNumber(phoneNumber))
            throw new ClinicApplicationException(ALREADY_REGISTERED_PHONE_NUMBER, phoneNumber);
    }

    private void assertPeselIsUnique(String pesel){
        if(patientRepository.existsByPesel(pesel) || doctorRepository.existsByPesel(pesel))
            throw new ClinicApplicationException(ALREADY_REGISTERED_PESEL, pesel);
    }
}
