package com.portfolio.expensetracker.util;

import lombok.*;
import org.springframework.http.HttpMethod;

public class AuthenticationConstants {

    public AuthenticationConstants() {
        throw new IllegalStateException("Cannot instantiate an util class.");
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Authentication {

        public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

        @Getter
        @ToString
        @RequiredArgsConstructor
        public enum Scope {

            // TODO: scope validation is not working
            EXPENSE_CREATE("EXPENSE_CREATE", HttpMethod.POST, "/api/v1/expenses");

            private final String value;
            private final HttpMethod httpMethod;
            private final String uri;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Jwt {

            /*
                HS512 algorithm (HMAC) key size is 512 Bits.
                1 Byte = 8 Bits.
                512 Bits / 8 Bits = 64 Bytes.

                In UTF-8 (character encoding), each character occupies 8 Bits = 1 Byte.
                Therefore, the JWT signature key must have 64 characters.
             */
            public static final int ALGORITHM_KEY_SIZE_BYTES = 64;
            public static final String PREFIX = "Bearer ";
            public static final String ISSUER = "auth8";
            public static final int ALLOWED_CLOCK_SKEW_SECONDS = 30;
            public static final String AUDIENCE = "expense-tracker";

            @Getter
            @ToString
            @RequiredArgsConstructor
            public enum Claim {

                SCOPE("scope"),
                DIGITAL_USER_ID("digitalUserId");

                private final String value;
            }
        }
    }
}
