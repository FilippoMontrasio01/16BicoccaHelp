package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Map;

public class CreateTutorRequest {
    public final @NonNull String corsoDiStudi;
    public final @NonNull Map<String, Boolean> disponibilitaGiorni;
    public final @NonNull ArrayList<String> skills;


    public CreateTutorRequest(@NonNull String corsoDiStudi,
                              @NonNull Map<String, Boolean> disponibilitaGiorni,
                              @NonNull ArrayList<String> skills) {
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilitaGiorni = disponibilitaGiorni;
        this.skills = skills;
    }
}
