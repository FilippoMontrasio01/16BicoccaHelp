package com.example.bicoccahelp.data.date;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;




import java.util.Map;
import java.util.Objects;

public class CreateDateRequest {
    private @NonNull Map<String, Boolean> disponibilitaOrari;
    private @NonNull Timestamp data;
    private @NonNull String uidTutor;



    public CreateDateRequest(@NonNull Map<String, Boolean> disponibilitaOrari,
                             @NonNull Timestamp data, String uidTutor) {
        this.disponibilitaOrari = Objects.requireNonNull(disponibilitaOrari, "disponibilitaOrari cannot be null");
        this.data = Objects.requireNonNull(data, "data cannot be null");
        this.uidTutor = Objects.requireNonNull(uidTutor, "uidTutor cannot be null");
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

    public void setDisponibilitaOrari(@NonNull Map<String, Boolean> disponibilitaOrari) {
        this.disponibilitaOrari = Objects.requireNonNull(disponibilitaOrari, "disponibilitaOrari cannot be null");
    }

    public void setData(@NonNull Timestamp data) {
        this.data = Objects.requireNonNull(data, "data cannot be null");
    }

    public void setUidTutor(@NonNull String uidTutor) {
        this.uidTutor = Objects.requireNonNull(uidTutor, "uidTutor cannot be null");
    }
}
