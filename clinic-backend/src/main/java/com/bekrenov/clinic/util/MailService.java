package com.bekrenov.clinic.util;

import com.bekrenov.clinic.dto.request.RegistrationRequest;
import com.bekrenov.clinic.model.entity.Appointment;
import com.bekrenov.clinic.model.enums.AppointmentStatus;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private static final String EMAIL_TEMPLATES_RESOURCE_PATH = "/email_templates";
    private static final String PENDING_APPOINTMENT_TEMPLATE_NAME = "/pending-appointment.txt";
    private static final String CONFIRMED_APPOINTMENT_TEMPLATE_NAME = "/confirmed-appointment.txt";
    private static final String CANCELLED_APPOINTMENT_TEMPLATE_NAME = "/cancelled-appointment.txt";

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${spring.custom.frontend-base-url}")
    private String frontendBaseUrl;

    public void sendEmailWithActivationLink(RegistrationRequest registration, String activationToken) {
        String contentTemplate = this.getContentTemplate(EMAIL_TEMPLATES_RESOURCE_PATH + "/activation.txt");
        String url = frontendBaseUrl + "/registration/confirmed?token=" + activationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(registration.email());
        message.setSubject("Activate your account");
        message.setFrom("MedicaPlus <" + fromAddress + ">");
        message.setText(String.format(contentTemplate, registration.firstName(), url));
        mailSender.send(message);
    }

    public void sendEmailWithAppointment(Appointment appointment){
        String templatePath = getTemplatePathDependingOnStatus(appointment.getStatus());
        String contentTemplate = this.getContentTemplate(templatePath);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appointment.getPatient().getEmail());
        message.setSubject("Appointment at MedicaPlus");
        message.setFrom("MedicaPlus <" + fromAddress + ">");
        message.setText(composeMessageContent(contentTemplate, appointment));
        mailSender.send(message);
    }

    private String composeMessageContent(String contentTemplate, Appointment appointment) {
        String patientName = appointment.getPatient().getFirstName();
        String department = appointment.getDepartment().nameAndAddress();
        String doctor = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
        String dateTime = appointment.getDate() + " " + appointment.getTime();
        return String.format(contentTemplate, patientName, department, doctor, dateTime);
    }

    private String getTemplatePathDependingOnStatus(AppointmentStatus status){
        String templatePath = switch (status) {
            case PENDING -> PENDING_APPOINTMENT_TEMPLATE_NAME;
            case CONFIRMED -> CONFIRMED_APPOINTMENT_TEMPLATE_NAME;
            case CANCELLED -> CANCELLED_APPOINTMENT_TEMPLATE_NAME;
            default -> throw new IllegalArgumentException();
        };
        return EMAIL_TEMPLATES_RESOURCE_PATH + templatePath;
    }

    private String getContentTemplate(String path) {
        var inputStream = getClass().getResourceAsStream(path);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
