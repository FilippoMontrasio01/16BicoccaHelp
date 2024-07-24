package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Map;

public class CreateTutorRequest {
    private final @NonNull String corsoDiStudi;
    private final @NonNull Map<String, Boolean> disponibilitaGiorni;
    private final @NonNull ArrayList<String> skills;

    private final @NonNull double averageReview;


    public CreateTutorRequest(@NonNull String corsoDiStudi,
                              @NonNull Map<String, Boolean> disponibilitaGiorni,
                              @NonNull ArrayList<String> skills,
                              @NonNull double averageReview) {
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilitaGiorni = disponibilitaGiorni;
        this.skills = skills;
        this.averageReview = averageReview;

    }

    @NonNull
    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }

    @NonNull
    public Map<String, Boolean> getDisponibilitaGiorni() {
        return disponibilitaGiorni;
    }

    @NonNull
    public ArrayList<String> getSkills() {
        return skills;
    }

    public double getAverageReview() {
        return averageReview;
    }
}
