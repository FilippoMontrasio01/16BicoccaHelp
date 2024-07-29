package com.example.bicoccahelp.utils;

import android.text.TextUtils;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
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


    private static CorsoDiStudiRepository corsoDiStudiRepository = ServiceLocator.getInstance()
            .getCorsoDiStudiRepository();



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

    public static void isValidStudyProgram(String studyProgram, Callback<Boolean> callback){
        //String nomeCorsoFormatted = studyProgram.toLowerCase();
        corsoDiStudiRepository.corsoDiStudiExists(studyProgram, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                callback.onSucces(exist);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    public static String capitalizeFirstLetter(String str) {

        if (str == null) {
            return null; // or throw IllegalArgumentException
        }


        str = str.toLowerCase();

        char[] chars = str.toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String formatDate(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getSeconds() * 1000);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Mese è 0-based, quindi aggiungi 1
        int year = calendar.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }

    public static int calculateDaysDifference(Timestamp lessonDate) {
        // Ottieni la data attuale come Timestamp
        Timestamp currentTimestamp = Timestamp.now();

        // Calcola la differenza in millisecondi tra le date
        long diffInMillis = lessonDate.getSeconds() * 1000 - currentTimestamp.getSeconds() * 1000;

        // Converti la differenza in giorni
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        // Restituisci la differenza come intero
        return (int) diffInDays;
    }


}
