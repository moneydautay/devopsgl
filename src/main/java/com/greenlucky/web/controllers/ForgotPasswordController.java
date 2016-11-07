package com.greenlucky.web.controllers;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@Controller
public class ForgotPasswordController {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordController.class);

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

    @Autowired
    private PasswordResetToke passwordResetToke;

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
    public String forgotPasswordGet(){

        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
    public String forgotPasswordPost(HttpRequest httpRequest, @RequestParam("email") String email, Model model){

        return EMAIL_ADDRESS_VIEW_NAME;
    }
}
