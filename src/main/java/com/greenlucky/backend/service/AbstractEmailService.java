package com.greenlucky.backend.service;
import com.greenlucky.web.domain.frontend.FeedbackPojo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by Mr Lam on 4/11/2016.
 */
@Service
public abstract class AbstractEmailService implements EmailService{



    @Value("${default.to.address}")
    private String defaultToAddress;

    /**
     * Creates a simple mail message from a feedback pojo
     * @param feedback The feedback pojo
     * @return
     */
    protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(FeedbackPojo feedback){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(defaultToAddress);
        message.setFrom(feedback.getEmail());
        message.setSubject("[Devops Greenlucky]: Feedback receive from "+ feedback.getFirstName() + " "+ feedback.getLastName()+"!");
        message.setText(feedback.getFeedback());
        return message;
    }

    @Override
    public void sendFeedbackEmail(FeedbackPojo feedback){
        sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackPojo(feedback));
    }
}
