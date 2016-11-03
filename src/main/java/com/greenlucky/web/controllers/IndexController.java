package com.greenlucky.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Mr Lam on 1/11/2016.
 */
@Controller
public class IndexController {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);


    @RequestMapping("/")
    public String home(){
        return "index";
    }
}

