package com.example.bicoccahelp.data.auth.authException;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuthException;

public class AuthException extends FirebaseAuthException {

    public static final String AUTH_GENERIC_ERROR = "AUTH_GENERIC_ERROR";


    public AuthException(@NonNull String errorCode, @NonNull String detailMessage) {
        super(AUTH_GENERIC_ERROR,"An error occurred during the authentication process");
    }
}
