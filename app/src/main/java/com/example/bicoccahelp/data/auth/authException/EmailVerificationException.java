package com.example.bicoccahelp.data.auth.authException;

import androidx.annotation.NonNull;

public class EmailVerificationException extends AuthException{

    private static final String EMAIL_VERIFICATION_ERROR = "EMAIL_VERIFICATION_ERROR";
    public EmailVerificationException(@NonNull String errorCode, @NonNull String detailMessage) {
        super(EMAIL_VERIFICATION_ERROR, "Failed to send email verification. Please " +
                "make sure you have provided a valid email address and try again");
    }
}
