package com.java.currencyexchange.errorhandling.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ServiceResponse implements Serializable {

    private final String reference = UUID.randomUUID().toString();

    private String errorMessage;

    private String errorCode;

}
