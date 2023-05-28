package com.bekrenov.clinic.mvc_controller;

import com.bekrenov.clinic.dto.ChangePasswordDTO;
import com.bekrenov.clinic.entity.Patient;
import com.bekrenov.clinic.service.ClinicUserDetailsService;
import com.bekrenov.clinic.service.PatientService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PasswordController {

    private final ClinicUserDetailsService userDetailsService;

    private final PatientService patientService;

    public PasswordController(ClinicUserDetailsService userDetailsService, PatientService patientService) {
        this.userDetailsService = userDetailsService;
        this.patientService = patientService;
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model, Authentication authentication){

        model.addAttribute("changePasswordDTO", new ChangePasswordDTO("", ""));

        Patient patient = patientService.findByEmail(authentication.getName());
        model.addAttribute("patient", patient);

        return "/common/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordDTO") ChangePasswordDTO changePasswordDTO){

        userDetailsService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());

        return "redirect:/login";
    }

}
