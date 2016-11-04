package com.greenlucky.web.controllers;

import com.greenlucky.backend.service.EmailService;
import com.greenlucky.web.domain.frontend.FeedbackPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Mr Lam on 4/11/2016.
 */
@Controller
public class ContactController {
    
    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactController.class);

    /** The contact us view name */
    private static final  String CONTACT_US_VIEW_NAME = "contact/contact";

    /** The key which identified  the feedback payload in the model.*/
    private static final String  FEEDBACK_MODEL_KEY = "feedback";

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    public String contactGet(Model model){
        FeedbackPojo feedbackPojo = new FeedbackPojo();
        model.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedbackPojo);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public String contactPost(@ModelAttribute(ContactController.FEEDBACK_MODEL_KEY) FeedbackPojo feedbackPojo){
        LOGGER.debug("Feedback Pojo content: {}", feedbackPojo);
        emailService.sendFeedbackEmail(feedbackPojo);
        return ContactController.CONTACT_US_VIEW_NAME;
    }
}
