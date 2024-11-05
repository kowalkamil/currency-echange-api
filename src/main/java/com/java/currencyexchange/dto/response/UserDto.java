package com.java.currencyexchange.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserDto {
    private String name;
    private String lastName;
    private String username;
    private BigDecimal accountBalancePln;
    private BigDecimal accountBalanceUsd;
}
