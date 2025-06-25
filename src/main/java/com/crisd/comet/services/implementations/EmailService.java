package com.crisd.comet.services.implementations;

import com.crisd.comet.dto.input.EmailDetailsDTO;
import com.crisd.comet.services.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;

    @Override
    public void SendMail(EmailDetailsDTO emailDetailsDTO, String templatePath) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("Comet");
        mimeMessageHelper.setTo(emailDetailsDTO.to());
        mimeMessageHelper.setSubject(emailDetailsDTO.subject());

        String htmlTemplate = readHtmlTemplate(templatePath);

        htmlTemplate = htmlTemplate.replace("{{username}}", emailDetailsDTO.name());
        htmlTemplate = htmlTemplate.replace("{{verification_code}}", emailDetailsDTO.verificationCode());

        mimeMessageHelper.setText(htmlTemplate, true);
        mailSender.send(mimeMessage);
    }

    private String readHtmlTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        byte[] bytes = resource.getInputStream().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
