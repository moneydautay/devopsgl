package com.greenlucky.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Mr Lam on 4/11/2016.
 */
@Controller
public class CopyController {

    @RequestMapping("/about")
    public String about(){
        return "copy/about";
    }
}
