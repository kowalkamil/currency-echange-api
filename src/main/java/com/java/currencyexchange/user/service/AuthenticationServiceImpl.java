package com.java.currencyexchange.user.service;

import com.java.currencyexchange.dto.request.SignInRequest;
import com.java.currencyexchange.dto.request.SignUpRequest;
import com.java.currencyexchange.dto.response.JwtAuthenticationResponse;
import com.java.currencyexchange.security.service.JwtService;
import com.java.currencyexchange.user.model.User;
import com.java.currencyexchange.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(final SignUpRequest request) {

        String accountIdentifier;
        do {
            accountIdentifier = createAccountIdentifier(request.getName(), request.getLastName());
        } while (userRepository.findByUsername(accountIdentifier).isPresent());

        var user = User.builder()
                .username(accountIdentifier)
                .name(request.getName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountBalancePln(request.getStartingBalancePln())
                .accountBalanceUsd(BigDecimal.ZERO)
                .build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().
                token(jwt)
                .username(accountIdentifier)
                .build();
    }

    @Override
    public JwtAuthenticationResponse signin(final SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().
                token(jwt)
                .username(user.getUsername())
                .build();
    }

    private String createAccountIdentifier(String name, String lastName) {
        Random random = new Random();
        return name.toLowerCase().substring(0, 3) + lastName.toLowerCase().substring(0, 3) + random.nextInt(10001);
    }
}
