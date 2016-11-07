package com.greenlucky.web.controllers;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToken;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.service.EmailService;
import com.greenlucky.backend.service.I18NService;
import com.greenlucky.backend.service.PasswordResetTokenService;
import com.greenlucky.backend.service.UserService;
import com.greenlucky.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

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

    private static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";

    private static final  String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";

    private static final String MESSAGE_ATTRIBUTE_NAME = "message";

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${webmaster.email}")
    private String webmasterEmail;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
    public String changePasswordGet(@RequestParam("id") long id,
                                    @RequestParam("token") String token,
                                    Locale locale, Model model){

        if(StringUtils.isEmpty(token) || id==0){
            LOGGER.error("Invalid user {} or token value {}", id, token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

        if(passwordResetToken == null){
            LOGGER.warn("A token couldn't be found with value {}", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = passwordResetToken.getUser();

        if(user.getId() != id){
            LOGGER.error("The user id {} passed a parameter does not match the user id {} associated with the token {}", id, user.getId(), token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        if(LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())){
            LOGGER.error("The token {} has expired", token);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        model.addAttribute("principalId", user.getId());

        //OK to proceed. We auto-authenticate the user so that in the POST request we can check if the user is authenticated
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return CHANGE_PASSWORD_VIEW_NAME;
    }

    @RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
    public String changeUserPasswordPost(@RequestParam("principal_id") long userId,
                                         @RequestParam("password") String password, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(null == auth){
            LOGGER.error("An unauthenticated user tried to invoke reset password POST method");
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform the request");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        User user = (User) auth.getPrincipal();

        if(user.getId() != userId){
            LOGGER.error("Security breach! User {} is trying to make a password reset request on behalf of {}", user.getId(), userId);
            model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
            model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform the request");
            return CHANGE_PASSWORD_VIEW_NAME;
        }

        userService.updateUserPassword(user.getId(), password);
        LOGGER.info("Password successfully to update for user {}", user.getUsername());

        model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

        return  CHANGE_PASSWORD_VIEW_NAME;

    }
}
