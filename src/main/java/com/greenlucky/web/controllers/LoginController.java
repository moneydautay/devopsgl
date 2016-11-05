package com.greenlucky.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Mr Lam on 5/11/2016.
 */
@Controller
public class LoginController {

    /** Login the view name */
    private static final String LOGIN_VIEW_NAME = "/user/login";


    @RequestMapping("/login")
    public String login() {
        return LOGIN_VIEW_NAME;
    }
}
