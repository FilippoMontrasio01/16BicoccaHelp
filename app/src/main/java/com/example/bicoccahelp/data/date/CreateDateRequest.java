package com.example.bicoccahelp.data.date;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import org.checkerframework.checker.units.qual.N;


import java.util.Map;

public class CreateDateRequest {
    private final @NonNull Map<String, Boolean> disponibilitaOrari;
    private final @NonNull Timestamp data;
    private final @NonNull String uidTutor;
    private @NonNull String uidStudent;


    public CreateDateRequest(@NonNull Map<String, Boolean> disponibilitaOrari,
                             @NonNull Timestamp data, String uidTutor,
                             @NonNull String uidStudent) {
        this.disponibilitaOrari = disponibilitaOrari;
        this.data = data;
        this.uidTutor = uidTutor;
        this.uidStudent = uidStudent;
    }

    @NonNull
    public String getUidStudent() {
        return uidStudent;
    }

    public void setUidStudent(@NonNull String uidStudent) {
        this.uidStudent = uidStudent;
    }

    @NonNull
    public Map<String, Boolean> getDisponibilitaOrari() {
        return disponibilitaOrari;
    }

    @NonNull
    public Timestamp getData() {
        return data;
    }

    @NonNull
    public String getUidTutor() {
        return uidTutor;
    }
}