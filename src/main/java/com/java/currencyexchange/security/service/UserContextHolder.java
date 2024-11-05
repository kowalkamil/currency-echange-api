package com.java.currencyexchange.security.service;

import com.java.currencyexchange.user.model.User;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class UserContextHolder {

    public static User getCurrentlyLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new AuthorizationServiceException("User not logged in");
        }
        return (User) authentication.getPrincipal();
    }
}
