package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

import java.util.Map;

public class TutorModel extends UserModel {


    public final @NonNull Map<String, Boolean> disponibilitaGiorni;
    public final @NonNull Map<Integer, Boolean> disponibilitaOre;
    
    public final @NonNull String corsoDiStudi;

    public final @NonNull String idInsegnante;


    public TutorModel(String uid, String email, boolean emailVerified, String name, Uri photoUri, 
                      @NonNull Map<String, Boolean> disponibilitaGiorni,
                      @NonNull Map<Integer, Boolean> disponibilitaOre, 
                      @NonNull String corsoDiStudi, @NonNull String idInsegnante) {
        super(uid, email, emailVerified, name, photoUri);
        this.disponibilitaOre = disponibilitaOre;
        this.corsoDiStudi = corsoDiStudi;
        this.idInsegnante = idInsegnante;
        this.disponibilitaGiorni = disponibilitaGiorni;
    }


    public void setDisponibilities(String day, boolean isAvailable){
        disponibilitaGiorni.put(day, isAvailable);
    }


}
