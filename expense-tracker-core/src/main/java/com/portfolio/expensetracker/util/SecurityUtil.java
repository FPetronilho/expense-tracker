package com.portfolio.expensetracker.util;

import com.portfolio.expensetracker.exception.InternalServerErrorException;
import com.portfolio.expensetracker.security.context.DigitalUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
        throw new IllegalStateException("Cannot instantiate a util class");
    }

    public static DigitalUser getDigitalUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof DigitalUser) {
            // now I can safely cast because the data type is assured by the if statement
            return (DigitalUser) principal;
        }

        throw new InternalServerErrorException("could not retrieve Digital User from Spring Security Context");
    }

}
