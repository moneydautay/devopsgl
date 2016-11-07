package com.greenlucky.web.controllers;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToken;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.service.EmailService;
import com.greenlucky.backend.service.I18NService;
import com.greenlucky.backend.service.PasswordResetTokenService;
import com.greenlucky.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@Controller
public class ForgotMyPasswordController {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

    public static final String MAIL_SENT_KEY = "mailSent";

    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";


    public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email}")
    private String webmasterEmail;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String forgotPasswordGet(){

        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    public String forgotPasswordPost(HttpServletRequest request, @RequestParam("email") String email, Model model){

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

        if(passwordResetToken == null){
            LOGGER.warn("Couldn't find a password reset token for email {}", email);
        }else{
            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();
            String passwordResetUrl = UserUtils.createPasswordResetUrl(request,user.getId(), token);
            LOGGER.debug("Password Reset Url {}", passwordResetUrl);

            String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());
            emailText += "\r\n"+passwordResetUrl;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("[Devopsgl]: How to Reset Your Password");
            message.setText(emailText);
            message.setFrom(webmasterEmail);

            emailService.sendGenericEmailMessage(message);

        }

        model.addAttribute(MAIL_SENT_KEY, "true");

        return EMAIL_ADDRESS_VIEW_NAME;
    }

}
