package com.crisd.comet.services.interfaces;

import com.crisd.comet.dto.input.EmailDetailsDTO;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface IEmailService {
    void SendMail(EmailDetailsDTO emailDetailsDTO, String templatePath) throws MessagingException, IOException;
}
