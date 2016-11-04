package com.greenlucky.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

/**
 * Mock implementation of an email service
 * Created by Mr Lam on 4/11/2016.
 */
public class MockEmailService extends AbstractEmailService{

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendGenericEmailMessage(SimpleMailMessage message){
        LOGGER.debug("Simulating an email service...");
        LOGGER.info(message.toString());
        LOGGER.debug("Email sent.");
    }
}
