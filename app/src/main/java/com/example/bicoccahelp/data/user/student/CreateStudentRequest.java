package com.example.bicoccahelp.data.user.student;

import android.net.Uri;

import androidx.annotation.NonNull;

public class CreateStudentRequest {



    public final @NonNull String corsoDiStudi;
    public final @NonNull boolean isTutor;


    public CreateStudentRequest(@NonNull String corsoDiStudi, @NonNull boolean isTutor) {
        this.corsoDiStudi = corsoDiStudi;
        this.isTutor = isTutor;
    }



}
