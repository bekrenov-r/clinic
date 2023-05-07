package com.bekrenov.clinic.mvc_controller;

import com.bekrenov.clinic.entity.*;
import com.bekrenov.clinic.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final ClinicUserDetailsService userDetailsService;

    @Autowired
    public PatientController(AppointmentService appointmentService, PatientService patientService,
                             DepartmentService departmentService, DoctorService doctorService,
                             ClinicUserDetailsService userDetailsService){
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.doctorService = doctorService;
        this.userDetailsService = userDetailsService;
    }

    // mapping for home page
    @GetMapping("/home")
    public String showHomePage(Model model, Authentication auth){
        String username = auth.getName();
        Patient patient = patientService.findByUsername(username);
        model.addAttribute("patient", patient);
        return "/patient/home-page";
    }

    // mapping to form for creating appointment (empty form)
    @GetMapping("/create-appointment")
    public String showFormForAdd(Model model){
        // add empty Appointment to model
        model.addAttribute("appointment", new Appointment());

        // add specializations to the model
        model.addAttribute("specializations", Arrays.asList(Department.Specializations.values()));

        // add departments to the model
        model.addAttribute("departments", departmentService.findAll());

        // direct to Appointment-form page
        return "/patient/appointment-form";
    }

    // mapping for updating appointment (pre-populated form)
    @GetMapping("/update-appointment")
    public String showFormForUpdate(@RequestParam("appointmentId") int id, Model model){
        // find appointment with this id in database
        Appointment appointment = appointmentService.findById(id);

        // add attributes to model
        model.addAttribute("appointment", appointment);
        model.addAttribute("specializations", Arrays.asList(Department.Specializations.values()));

        // map to appointment-form page
        return "/patient/appointment-form";
    }

    // mapping for showing list of Appointments
    @GetMapping("/appointments")
    public String showAppointments(Model model, Authentication auth){
        Patient patient = patientService.findByUsername(auth.getName());
        List<Appointment> appointments = patient.getAppointments();
        // add list of appointments to the model
        model.addAttribute("appointments", appointments);
        return "/patient/list-appointments";
    }

    // mapping for saving Appointment from Appointment-form
    @PostMapping("/save-appointment")
    public String saveAppointment(@ModelAttribute("appointment") Appointment appointment, Authentication authentication){

        // get patient from spring security, reattach it to persistence context and add to Appointment object
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        Patient patient = patientService.findByUsername(userDetails.getUsername());
        appointment.setPatient(patient);

        // reattaching department to persistence context (to avoid PersistentObjectException)
        Department department = departmentService.findById(appointment.getDepartment().getId());
        appointment.setDepartment(department);

        // if user chose specific doctor, reattach it to persistence context and add to appointment
        // otherwise pick any doctor who can accept this appointment
        Doctor doctor = appointment.isAnyDoctor() || appointment.getDoctor() == null
                ? doctorService.getAnyDoctorForAppointment(appointment)
                : doctorService.findById(appointment.getDoctor().getId());
        appointment.setDoctor(doctor);

        System.out.println(appointment);
        appointmentService.save(appointment);
        return "redirect:/patient/appointments";
    }

    // mapping for deleting appointment
    @GetMapping("/cancel-appointment")
    public String deleteAppointment(@RequestParam("appointmentId") int id){
        appointmentService.deleteById(id);
        return "redirect:/patient/appointments";
    }

    // mapping for showing patient profile
    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication){
        // get patient from spring security and reattach it to persistence context
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        Patient patient = patientService.findByUsername(userDetails.getUsername());
        model.addAttribute("patient", patient);
        return "/patient/patient-profile";
    }

    @PostMapping("/create-profile")
    public String createProfile(@ModelAttribute("registration") Registration registration){
        System.out.println(registration);
        patientService.createPatientAndUser(registration);
        return "redirect:/login";
    }

    // mapping for saving patient profile
    @PostMapping("/save-profile")
    public String saveProfile(@ModelAttribute("patient") Patient patient){

        String username = patientService.findById(patient.getId()).getUsername();

        // if patient changed his email
        if(!username.equals(patient.getEmail())){
            // change user's username
            userDetailsService.changeUsername(username, patient.getEmail());
        }

        // set patient's username to value of email
        patient.setUsername(patient.getEmail());

        patientService.save(patient);
        return "redirect:/patient/profile";
    }

    // mapping for deleting patient profile
    @GetMapping("/delete-profile")
    public String deleteProfile(){
        return "redirect:/login"; // after deleting profile user should be redirected to login page
    }
}
