package org.oc.paymybuddy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        try {
            Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    }

    public record LoginRequest(String username, String password) {
    }

}