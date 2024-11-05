package com.java.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.currencyexchange.dto.request.SignInRequest;
import com.java.currencyexchange.dto.request.SignUpRequest;
import com.java.currencyexchange.dto.response.JwtAuthenticationResponse;
import com.java.currencyexchange.security.controller.AccountController;
import com.java.currencyexchange.security.service.JwtService;
import com.java.currencyexchange.security.service.UserService;
import com.java.currencyexchange.user.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;



    @Test
    void shouldRegisterUser() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("Kamil")
                .lastName("Kowalski")
                .password("password")
                .startingBalancePln(BigDecimal.valueOf(100.0))
                .build();

        JwtAuthenticationResponse jwtAuthenticationResponse = JwtAuthenticationResponse.builder()
                .username("kamkow2811")
                .token("eyJeqweqwe1NiJ9.eyJzdWIiqweqwtdsfODM2ODc3LCJleHAiOjE3MzA4MzgzMTd9.uxYq8CqweqetxfXxxMLr5Pdm78CRY")
                .build();

        doReturn(jwtAuthenticationResponse).when(authenticationService).signup(request);

        mockMvc.perform(post("/api/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"token\": \"eyJeqweqwe1NiJ9.eyJzdWIiqweqwtdsfODM2ODc3LCJleHAiOjE3MzA4MzgzMTd9.uxYq8CqweqetxfXxxMLr5Pdm78CRY\",\n" +
                        "    \"username\": \"kamkow2811\"\n" +
                        "}"));
    }

    @Test
    void shouldLoginUser() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .username("kamkow2811")
                .password("password")
                .build();

        JwtAuthenticationResponse jwtAuthenticationResponse = JwtAuthenticationResponse.builder()
                .username("kamkow2811")
                .token("eyJeqweqwe1NiJ9.eyJzdWIiqweqwtdsfODM2ODc3LCJleHAiOjE3MzA4MzgzMTd9.uxYq8CqweqetxfXxxMLr5Pdm78CRY")
                .build();

        doReturn(jwtAuthenticationResponse).when(authenticationService).signin(request);

        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"token\": \"eyJeqweqwe1NiJ9.eyJzdWIiqweqwtdsfODM2ODc3LCJleHAiOjE3MzA4MzgzMTd9.uxYq8CqweqetxfXxxMLr5Pdm78CRY\",\n" +
                        "    \"username\": \"kamkow2811\"\n" +
                        "}"));
    }
}
