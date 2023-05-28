package com.bekrenov.clinic.rest_controller;

import com.bekrenov.clinic.service.ClinicUserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passwords")
public class PasswordRestController {

    private final ClinicUserDetailsService userDetailsService;

    public PasswordRestController(ClinicUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/is-correct")
    public boolean passwordIsCorrect(@RequestParam("username") String username,
                                     @RequestParam("password") String password) {
        return userDetailsService.passwordIsCorrect(username, password);
    }

}
