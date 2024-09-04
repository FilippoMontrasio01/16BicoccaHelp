package com.example.bicoccahelp.data.date;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import org.checkerframework.checker.units.qual.N;


import java.util.Map;

public class DateModel {


    private @NonNull Map<String, Boolean> disponibilitaOrari;
    private @NonNull Timestamp data;
    private @NonNull String uidTutor;



    public DateModel(@NonNull Map<String, Boolean> disponibilitaOrari, @NonNull Timestamp data,
                     @NonNull String uidTutor) {
        this.disponibilitaOrari = disponibilitaOrari;
        this.data = data;
        this.uidTutor = uidTutor;
    }

    public void setDisponibilitaOrari(@NonNull Map<String, Boolean> disponibilitaOrari) {
        this.disponibilitaOrari = disponibilitaOrari;
    }

    public void setData(@NonNull Timestamp data) {
        this.data = data;
    }

    public void setUidTutor(@NonNull String uidTutor) {
        this.uidTutor = uidTutor;
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
