package com.greenlucky.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Mr Lam on 5/11/2016.
 */
@Controller
public class PayloadController {

    /** Payload view name */
    private static final String PAYLOAD_VIEW_NAME = "/payload/payload";

    @RequestMapping("/payload")
    public String payload(){
        return PAYLOAD_VIEW_NAME;
    }
}
