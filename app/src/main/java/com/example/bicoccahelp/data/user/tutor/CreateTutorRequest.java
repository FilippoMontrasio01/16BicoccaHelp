package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Map;

public class CreateTutorRequest {
    public final @NonNull String corsoDiStudi;
    public final @NonNull Map<String, Boolean> disponibilitaGiorni;
    public final @NonNull Map<Integer, Boolean> disponibilitaOre;

    public CreateTutorRequest(@NonNull String corsoDiStudi,
                              @NonNull Map<String, Boolean> disponibilitaGiorni,
                              @NonNull Map<Integer, Boolean> disponibilitaOre) {
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilitaGiorni = disponibilitaGiorni;
        this.disponibilitaOre = disponibilitaOre;
    }
}
