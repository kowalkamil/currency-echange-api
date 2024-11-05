package com.java.currencyexchange.user.service;

import com.java.currencyexchange.common.service.RestClient;
import com.java.currencyexchange.dto.response.UserDto;
import com.java.currencyexchange.security.service.UserContextHolder;
import com.java.currencyexchange.user.enums.Currency;
import com.java.currencyexchange.user.mapper.UserMapper;
import com.java.currencyexchange.user.model.User;
import com.java.currencyexchange.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RestClient restClient;

    @Value("${currency.api.url}")
    private String apiUrlTemplate;

    @Override
    public UserDto getUserDto() {
        final Long userId = UserContextHolder.getCurrentlyLoggedInUser().getId();
        return this.userMapper.toDto(this.userRepository.findById(userId)
                .orElseThrow());
    }

    @Override
    public UserDto exchangeCurrency(final Currency currency, final BigDecimal amount) {
        final Long userId = UserContextHolder.getCurrentlyLoggedInUser().getId();
        User user = this.userRepository.getReferenceById(userId);

        if (!currency.equals(Currency.USD) && !currency.equals(Currency.PLN)) {
            throw new RuntimeException("Wrong currency provided");
        }

        BigDecimal currencyRate = getCurrencyRate(Currency.USD);
        BigDecimal accountBalance = currency.equals(Currency.USD) ? user.getAccountBalanceUsd() : user.getAccountBalancePln();
        BigDecimal targetBalance = currency.equals(Currency.USD) ? user.getAccountBalancePln() : user.getAccountBalanceUsd();
        BigDecimal adjustedAmount = currency.equals(Currency.USD) ? amount.multiply(currencyRate) : amount.divide(currencyRate, RoundingMode.HALF_UP);

        if (accountBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (currency.equals(Currency.USD)) {
            user.setAccountBalanceUsd(accountBalance.subtract(amount));
            user.setAccountBalancePln(targetBalance.add(adjustedAmount));
        } else {
            user.setAccountBalancePln(accountBalance.subtract(amount));
            user.setAccountBalanceUsd(targetBalance.add(adjustedAmount));
        }

        this.userRepository.save(user);

        return this.userMapper.toDto(user);
    }

    private BigDecimal getCurrencyRate(Currency currency) {
        String apiUrl = apiUrlTemplate.replace("{currency}", currency.getValue());
        final JSONObject jsonResponse = new JSONObject(this.restClient.getObject(apiUrl, String.class));
        return BigDecimal.valueOf(jsonResponse
                .getJSONArray("rates")
                .getJSONObject(0)
                .getDouble("mid"));
    }
}
