package com.java.currencyexchange.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.currencyexchange.errorhandling.dto.ServiceResponse;
import com.java.currencyexchange.errorhandling.exception.GeneralServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RestClient {

    private final WebClient webClient;

    private final ObjectMapper mapper;

    public <R> R getObject(String url, Class<R> returnType) {
        return this.webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(returnType)
                .block();
    }

    private Mono<Throwable> handleError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).flatMap(body -> {
            try {
                return Mono.error(GeneralServiceException.builder()
                        .errorCode(this.mapper.readValue(body.getBytes(), ServiceResponse.class).getErrorCode())
                        .errorMessage(this.mapper.readValue(body.getBytes(), ServiceResponse.class).getErrorMessage())
                        .build());
            } catch (Exception e) {
                return Mono.error(new RuntimeException("There is a problem with processing your request"));
            }
        });
    }
}
