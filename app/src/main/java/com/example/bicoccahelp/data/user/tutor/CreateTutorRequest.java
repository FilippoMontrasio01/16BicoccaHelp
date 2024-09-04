package com.example.bicoccahelp.data.user.tutor;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class CreateTutorRequest {
    private @NonNull String corsoDiStudi;
    private @NonNull Map<String, Boolean> disponibilitaGiorni;
    private @NonNull ArrayList<String> skills;

    private double averageReview;


    public CreateTutorRequest(@NonNull String corsoDiStudi,
                              @NonNull Map<String, Boolean> disponibilitaGiorni,
                              @NonNull ArrayList<String> skills,
                              double averageReview) {
        this.corsoDiStudi = Objects.requireNonNull(corsoDiStudi, "corsoDiStudi cannot be null");
        this.disponibilitaGiorni = Objects.requireNonNull(disponibilitaGiorni, "disponibilitaGiorni cannot be null");
        this.skills = Objects.requireNonNull(skills, "skills cannot be null");
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

    public void setCorsoDiStudi(@NonNull String corsoDiStudi) {
        this.corsoDiStudi = Objects.requireNonNull(corsoDiStudi, "corsoDiStudi cannot be null");
    }

    public void setDisponibilitaGiorni(@NonNull Map<String, Boolean> disponibilitaGiorni) {
        this.disponibilitaGiorni = Objects.requireNonNull(disponibilitaGiorni, "disponibilitaGiorni cannot be null");
    }

    public void setSkills(@NonNull ArrayList<String> skills) {
        this.skills = Objects.requireNonNull(skills, "skills cannot be null");
    }

    public void setAverageReview(double averageReview) {
        this.averageReview = averageReview;
    }
}
