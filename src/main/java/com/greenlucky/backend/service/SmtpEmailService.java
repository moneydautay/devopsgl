package com.greenlucky.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Mr Lam on 4/11/2016.
 */
public class SmtpEmailService extends AbstractEmailService {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SmtpEmailService.class);

    @Autowired
    private MailSender mailSender;

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message){
        LOGGER.debug("Sending email for {}", message);
        mailSender.send(message);
        LOGGER.info("Email sent.");
    }
}
