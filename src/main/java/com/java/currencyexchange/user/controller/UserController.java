package com.java.currencyexchange.user.controller;

import com.java.currencyexchange.dto.response.UserDto;
import com.java.currencyexchange.user.enums.Currency;
import com.java.currencyexchange.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final AccountService accountService;

    @GetMapping
    public UserDto getUserDetails() {
        return this.accountService.getUserDto();
    }

    @PostMapping("/exchange")
    public UserDto exchangeCurrency(@RequestParam Currency currency,
                                    @RequestParam BigDecimal amount) {
        return this.accountService.exchangeCurrency(currency, amount);
    }
}
