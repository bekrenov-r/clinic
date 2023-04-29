package com.bekrenov.clinic.mvc_controller;

import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.entity.Registration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage(){
        return "/common/login";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        Registration registration = new Registration(new Patient(), null); // todo: check if this is necessary
        model.addAttribute("registration", registration);
        return "/patient/registration";
    }

}
