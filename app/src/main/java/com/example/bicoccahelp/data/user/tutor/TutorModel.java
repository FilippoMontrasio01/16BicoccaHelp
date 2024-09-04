package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Map;

public class TutorModel extends UserModel {


    private @NonNull Map<String, Boolean> disponibilitaGiorni;
    
    private @NonNull String corsoDiStudi;
    private @NonNull ArrayList<String> skills;
    private double averageReview;


    public TutorModel(String uid, String email, boolean emailVerified, String name, Uri photoUri,
                      @NonNull Map<String, Boolean> disponibilitaGiorni,
                      @NonNull String corsoDiStudi, @NonNull ArrayList<String> skills,
                      double averageReview) {
        super(uid, email, emailVerified, name, photoUri != null ? photoUri : Uri.parse(""));
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilitaGiorni = disponibilitaGiorni;
        this.skills = skills;
        this.averageReview = averageReview;
    }


    public void setDisponibilities(String day, boolean isAvailable){
        disponibilitaGiorni.put(day, isAvailable);
    }

    @NonNull
    public Map<String, Boolean> getDisponibilitaGiorni() {
        return disponibilitaGiorni;
    }

    @NonNull
    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }

    @NonNull
    public ArrayList<String> getSkills() {
        return skills;
    }

    public double getAverageReview() {
        return averageReview;
    }

    public void setDisponibilitaGiorni(@NonNull Map<String, Boolean> disponibilitaGiorni) {
        this.disponibilitaGiorni = disponibilitaGiorni;
    }

    public void setCorsoDiStudi(@NonNull String corsoDiStudi) {
        this.corsoDiStudi = corsoDiStudi;
    }

    public void setSkills(@NonNull ArrayList<String> skills) {
        this.skills = skills;
    }

    public void setAverageReview(double averageReview) {
        this.averageReview = averageReview;
    }
}
