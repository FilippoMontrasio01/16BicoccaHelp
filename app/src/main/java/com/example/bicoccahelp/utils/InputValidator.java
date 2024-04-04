package com.example.bicoccahelp.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@campus\\.unimib\\.it$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-z]).{8,}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    public static boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return !TextUtils.isEmpty(email) && matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches() && password.length()>=8;
    }

    public static boolean isValidName(String name){
        return name.length() >= 3;
    }


}
