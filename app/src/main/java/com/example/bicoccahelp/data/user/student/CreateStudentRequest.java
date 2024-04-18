package com.example.bicoccahelp.data.user.student;

import androidx.annotation.NonNull;

public class CreateStudentRequest {



    public final @NonNull String corsoDiStudi;
    public final boolean isTutor;


    public CreateStudentRequest(@NonNull String corsoDiStudi, boolean isTutor) {
        this.corsoDiStudi = corsoDiStudi;
        this.isTutor = isTutor;
    }



}
