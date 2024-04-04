package com.example.bicoccahelp.data.auth.authException;

import androidx.annotation.NonNull;

public class PasswordResetException extends AuthException{

    private static final String PASSWORD_RESET_ERROR = "PASSWORD_RESET_ERROR";

    public PasswordResetException(@NonNull String errorCode, @NonNull String detailMessage) {
        super(PASSWORD_RESET_ERROR, "Failed to send password reset email. Please try again later");
    }
}
