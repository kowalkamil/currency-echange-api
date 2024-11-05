package com.java.currencyexchange.errorhandling.handler;

import com.java.currencyexchange.errorhandling.enums.ErrorCodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ExceptionResponse handleRemainingExceptions(final Exception ex) {
        return getExceptionResponse(ex);
    }

    private ExceptionResponse getExceptionResponse(Exception ex) {
        var defaultCode = ErrorCodeEnum.UNKNOWN_ERROR.getCode();
        val exceptionResponse = new ExceptionResponse(defaultCode, ex.getMessage());
        log.error("Exception id: %s, errorCode: %s".formatted(exceptionResponse.getUuid(), defaultCode), ex);
        return exceptionResponse;
    }

    @RequiredArgsConstructor
    @Getter
    public static class ExceptionResponse {
        private final String code;
        private final String message;
        private final OffsetDateTime timestamp = OffsetDateTime.now();
        private final String uuid = UUID.randomUUID().toString();
    }
}
