package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Map;

public class TutorModel extends UserModel {


    private final @NonNull Map<String, Boolean> disponibilitaGiorni;
    
    private final @NonNull String corsoDiStudi;
    private final @NonNull ArrayList<String> skills;


    public TutorModel(String uid, String email, boolean emailVerified, String name, Uri photoUri,
                      @NonNull Map<String, Boolean> disponibilitaGiorni,
                      @NonNull String corsoDiStudi, @NonNull ArrayList<String> skills) {
        super(uid, email, emailVerified, name, photoUri != null ? photoUri : Uri.parse(""));
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilitaGiorni = disponibilitaGiorni;
        this.skills = skills;
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
}
