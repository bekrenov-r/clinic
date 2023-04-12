package com.bekrenov.clinic.mvc_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("")
    public String redirectToHomePage(){
        return "redirect:/patient/home";
    }

}
