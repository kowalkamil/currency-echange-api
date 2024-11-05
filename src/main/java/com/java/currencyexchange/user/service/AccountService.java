package com.java.currencyexchange.user.service;

import com.java.currencyexchange.dto.response.UserDto;
import com.java.currencyexchange.user.enums.Currency;

import java.math.BigDecimal;

public interface AccountService {
    UserDto getUserDto();
    UserDto exchangeCurrency(Currency currency, BigDecimal amount);
}
