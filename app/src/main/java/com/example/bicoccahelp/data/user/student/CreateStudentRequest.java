package com.example.bicoccahelp.data.user.student;

import androidx.annotation.NonNull;

import org.checkerframework.checker.units.qual.N;

public class CreateStudentRequest {



    private final @NonNull String corsoDiStudi;
    private final  boolean isTutor;


    public CreateStudentRequest(@NonNull String corsoDiStudi,  boolean isTutor) {
        this.corsoDiStudi = corsoDiStudi;
        this.isTutor = isTutor;
    }

    @NonNull
    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }

    public boolean isTutor() {
        return isTutor;
    }
}
