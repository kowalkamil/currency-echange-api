package com.java.currencyexchange.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD("usd"),
    PLN("pln");

    private final String value;
}
