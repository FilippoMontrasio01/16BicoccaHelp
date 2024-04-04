package com.example.bicoccahelp.data.auth.authException;

import androidx.annotation.NonNull;

public class EmailAlreadyExistsException extends AuthException{

    private static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";

    public EmailAlreadyExistsException(@NonNull String errorCode, @NonNull String detailMessage) {
        super(EMAIL_ALREADY_EXISTS, "This email address is already associated with an existing " +
                "account. Please use a different email or try logging in");
    }
}
