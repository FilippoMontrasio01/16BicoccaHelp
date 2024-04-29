package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Map;

public class TutorModel extends UserModel {


    public final @NonNull Map<String, Boolean> disponibilitaGiorni;
    
    public final @NonNull String corsoDiStudi;
    public final @NonNull ArrayList<String> skills;


    public TutorModel(String uid, String email, boolean emailVerified, String name, Uri photoUri, 
                      @NonNull Map<String, Boolean> disponibilitaGiorni,
                      @NonNull String corsoDiStudi, @NonNull ArrayList<String> skills) {
        super(uid, email, emailVerified, name, photoUri);
        this.corsoDiStudi = corsoDiStudi;
        this.disponibilitaGiorni = disponibilitaGiorni;
        this.skills = skills;
    }


    public void setDisponibilities(String day, boolean isAvailable){
        disponibilitaGiorni.put(day, isAvailable);
    }


}
