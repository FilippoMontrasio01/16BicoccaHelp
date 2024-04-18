package com.example.bicoccahelp.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@campus\\.unimib\\.it$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-z]).{8,}$";
    private static final String NAME_REGEX = "^[a-zA-Z\\s'-]{3,}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX,
            Pattern.CASE_INSENSITIVE);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern namePattern = Pattern.compile(NAME_REGEX);



    //La mail può contenere SOLO il dominio @campus.unimib.it
    public static boolean isValidEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return !TextUtils.isEmpty(email) && matcher.matches();
    }

    /* La password deve essere lunga almeno 8 caratteri e:
        - Avere almeno una maiuscola
        - Avere almeno un numero
        - Avere almeno un carattere speciale
     */
    public static boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches() && password.length()>=8;
    }


    //Il nome può contenere tutti i caratteri, tranne i caratteri speciali e avere una
    // lunghezza minima di 3 caratteri
    public static boolean isValidName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return !TextUtils.isEmpty(name)  && matcher.matches();
    }


}
