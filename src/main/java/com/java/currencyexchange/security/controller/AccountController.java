package com.java.currencyexchange.security.controller;

import com.java.currencyexchange.dto.request.SignInRequest;
import com.java.currencyexchange.dto.request.SignUpRequest;
import com.java.currencyexchange.dto.response.JwtAuthenticationResponse;
import com.java.currencyexchange.user.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtAuthenticationResponse registerAccount(@Valid @RequestBody final SignUpRequest signUpRequest) {
        return this.authenticationService.signup(signUpRequest);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@Valid @RequestBody final SignInRequest signInRequest) {
        return this.authenticationService.signin(signInRequest);
    }
}
