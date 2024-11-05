package com.java.currencyexchange.integration;

import com.java.currencyexchange.user.enums.Currency;
import com.java.currencyexchange.user.model.User;
import com.java.currencyexchange.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void setUp() {
        User user = User.builder()
                .id(1L)
                .username("kamkow1293")
                .name("Kamil")
                .lastName("Kowalski")
                .accountBalanceUsd(BigDecimal.ZERO)
                .accountBalancePln(BigDecimal.valueOf(1000.00))
                .password("passwd")
                .build();

        this.userRepository.save(user);
    }

    @Test
    void shouldGetDetails() throws Exception {
        mockMvc.perform(get("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(User.builder()
                                .id(1L)
                                .username("kamkow1293")
                                .name("Kamil")
                                .lastName("Kowalski")
                                .accountBalanceUsd(BigDecimal.ZERO)
                                .accountBalancePln(BigDecimal.valueOf(1000.00))
                                .password("passwd")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kamil"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.username").value("kamkow1293"))
                .andExpect(jsonPath("$.accountBalancePln").value(1000.00))
                .andExpect(jsonPath("$.accountBalanceUsd").value(0.00));

    }

    @Test
    void shouldExchangeCurrencyAndUpdateDatabase() throws Exception {

        String currency = Currency.PLN.name();
        BigDecimal amount = new BigDecimal("100.00");

        mockMvc.perform(post("/api/v1/user/exchange")
                        .param("currency", currency)
                        .param("amount", amount.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(User.builder()
                                .id(1L)
                                .username("kamkow1293")
                                .name("Kamil")
                                .lastName("Kowalski")
                                .accountBalanceUsd(BigDecimal.ZERO)
                                .accountBalancePln(BigDecimal.valueOf(1000.00))
                                .password("passwd")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.username").value("kamkow1293"))
                .andExpect(jsonPath("$.accountBalancePln").value(900.00));

        User user = this.userRepository.findById(1L).get();

        assertAll(
                () -> assertTrue(user.getAccountBalancePln().compareTo(BigDecimal.valueOf(900.0)) == 0),
                () -> assertNotEquals(user.getAccountBalanceUsd(), BigDecimal.ZERO)
        );
    }
}
