package com.bekrenov.clinic.controller;

import com.bekrenov.clinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/activate")
    public ResponseEntity<Void> activateUser(@RequestParam String token){
        userService.activateUser(token);
        return ResponseEntity.ok().build();
    }
}
