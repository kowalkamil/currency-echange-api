package com.java.currencyexchange.user.service;

import com.java.currencyexchange.dto.request.SignInRequest;
import com.java.currencyexchange.dto.request.SignUpRequest;
import com.java.currencyexchange.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
