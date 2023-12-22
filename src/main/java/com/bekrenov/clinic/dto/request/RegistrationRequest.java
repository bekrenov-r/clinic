package com.bekrenov.clinic.dto.request;

public interface RegistrationRequest {
    String firstName();
    String lastName();
    String pesel();
    String email();
    String phoneNumber();
    String password();
    AddressRequest address();
}
