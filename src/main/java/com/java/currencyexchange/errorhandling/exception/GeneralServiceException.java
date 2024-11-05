package com.java.currencyexchange.errorhandling.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeneralServiceException extends RuntimeException {
    private String errorMessage;
    private String errorCode;
    private Integer requestHashCode;
}
