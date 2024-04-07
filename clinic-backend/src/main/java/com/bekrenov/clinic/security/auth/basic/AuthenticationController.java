package com.bekrenov.clinic.security.auth.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @RequestParam("username") String username, @RequestParam("password") String password
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(username, password));
    }
}
