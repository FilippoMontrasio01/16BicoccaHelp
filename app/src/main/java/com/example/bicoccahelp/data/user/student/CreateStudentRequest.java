package com.example.bicoccahelp.data.user.student;

import androidx.annotation.NonNull;



import java.util.Objects;

public class CreateStudentRequest {

    private @NonNull String corsoDiStudi;
    private boolean isTutor;


    public CreateStudentRequest(@NonNull String corsoDiStudi,  boolean isTutor) {
        this.corsoDiStudi = Objects.requireNonNull(corsoDiStudi, "Corso Di Studi Cannot be null");
        this.isTutor = isTutor;
    }

    @NonNull
    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }

    public boolean isTutor() {
        return isTutor;
    }

    public void setCorsoDiStudi(@NonNull String corsoDiStudi) {
        this.corsoDiStudi = Objects.requireNonNull(corsoDiStudi, "Corso Di Studi Cannot be null");
    }

    public void setTutor(boolean tutor) {
        isTutor = tutor;
    }
}
