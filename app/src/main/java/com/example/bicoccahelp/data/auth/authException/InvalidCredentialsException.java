package com.example.bicoccahelp.data.auth.authException;

import androidx.annotation.NonNull;

public class InvalidCredentialsException extends AuthException {

    private static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    public InvalidCredentialsException(@NonNull String errorCode, @NonNull String detailMessage) {
        super(INVALID_CREDENTIALS, "Invalid email or password. Please check " +
                "your credentials and try again");
    }
}
