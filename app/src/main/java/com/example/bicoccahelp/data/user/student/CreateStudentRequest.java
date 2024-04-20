package com.example.bicoccahelp.data.user.student;

import androidx.annotation.NonNull;

import org.checkerframework.checker.units.qual.N;

public class CreateStudentRequest {



    public final @NonNull String corsoDiStudi;
    public final  boolean isTutor;


    public CreateStudentRequest(@NonNull String corsoDiStudi,  boolean isTutor) {
        this.corsoDiStudi = corsoDiStudi;
        this.isTutor = isTutor;
    }



}
