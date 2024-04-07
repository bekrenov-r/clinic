package com.bekrenov.clinic.validation;

import com.bekrenov.clinic.exception.ClinicApplicationException;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.entity.Doctor;
import com.bekrenov.clinic.model.entity.Patient;
import com.bekrenov.clinic.model.entity.Person;
import com.bekrenov.clinic.model.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.bekrenov.clinic.exception.reason.ClinicApplicationExceptionReason.*;

public class AppointmentAssert {
    public static void assertPersonIsAppointmentOwner(Appointment appointment, Person person){
        if(person instanceof Doctor doctor)
            assertDoctorIsAppointmentOwner(appointment, doctor);
        else if(person instanceof Patient patient)
            assertPatientIsAppointmentOwner(appointment, patient);
        else throw new IllegalArgumentException("Only Doctor and Patient instances allowed here");
    }
    public static void assertPatientIsAppointmentOwner(Appointment appointment, Patient patient) {
        if(!appointment.getPatient().equals(patient))
            throw new ClinicApplicationException(NOT_ENTITY_OWNER);
    }

    public static void assertDoctorIsAppointmentOwner(Appointment appointment, Doctor doctor){
        if(!appointment.getDoctor().equals(doctor))
            throw new ClinicApplicationException(NOT_ENTITY_OWNER);
    }

    public static void assertAppointmentCanBeCancelled(Appointment appointment) {
        AppointmentStatus status = appointment.getStatus();
        if(status.equals(AppointmentStatus.CANCELLED) || status.equals(AppointmentStatus.FINISHED))
            throw new ClinicApplicationException(CANNOT_CANCEL_APPOINTMENT);
    }

    public static void assertAppointmentCanBeFinished(Appointment appointment) {
        if(!appointment.getStatus().equals(AppointmentStatus.CONFIRMED))
            throw new ClinicApplicationException(CANNOT_FINISH_APPOINTMENT);
    }

    public static void assertAppointmentCanBeConfirmed(Appointment appointment) {
        if(!appointment.getStatus().equals(AppointmentStatus.PENDING))
            throw new ClinicApplicationException(CANNOT_CONFIRM_APPOINTMENT);
    }

    public static void assertPatientHasNoAppointmentAtDateTime(Patient patient, LocalDate date, LocalTime time) {
        boolean patientHasAppointmentAtDateTime = patient.getAppointments()
                .stream()
                .anyMatch(a -> a.getDate().equals(date) && a.getTime().equals(time));
        if(patientHasAppointmentAtDateTime)
            throw new ClinicApplicationException(PATIENT_ALREADY_HAS_APPOINTMENT_AT_DATETIME, date, time);
    }
}
