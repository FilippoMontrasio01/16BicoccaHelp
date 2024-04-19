package com.example.bicoccahelp.data.user.student;

import androidx.annotation.NonNull;

public class CreateStudentRequest {



    public final @NonNull String corsoDiStudi;
    public boolean isTutor = false;


    public CreateStudentRequest(@NonNull String corsoDiStudi) {
        this.corsoDiStudi = corsoDiStudi;
    }



}
