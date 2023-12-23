package com.bekrenov.clinic.util;

import com.bekrenov.clinic.dto.request.RegistrationRequest;
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
    private static final String CONTENT_TEMPLATE_RESOURCE_PATH = "/activation-email.txt";

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void sendEmailWithActivationLink(RegistrationRequest registration, String activationToken) {
        String contentTemplate = this.getContentTemplate();
        String url = "http://localhost:8080/api/v1/users/activate?token=" + activationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(registration.email());
        message.setSubject("Activate your account");
        message.setFrom("MedicaPlus <" + fromAddress + ">");
        message.setText(String.format(contentTemplate, registration.firstName(), url));
        mailSender.send(message);
    }

    private String getContentTemplate() {
        var inputStream = getClass().getResourceAsStream(CONTENT_TEMPLATE_RESOURCE_PATH);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
