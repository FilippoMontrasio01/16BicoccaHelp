package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

import java.util.HashMap;
import java.util.Map;

public class TutorModel extends UserModel {


    public final @NonNull Map<String, Boolean> disponibilities;
    public final @NonNull String corsoDiStudi;

    public final @NonNull String idInsegnante;


    public TutorModel(String uid, String email, boolean emailVerified, String name, Uri photoUri, Map<String, Boolean> disponibilities, @NonNull String corsoDiStudi, @NonNull String idInsegnante) {
        super(uid, email, emailVerified, name, photoUri);
        this.corsoDiStudi = corsoDiStudi;
        this.idInsegnante = idInsegnante;
        this.disponibilities = new HashMap<>();
    }


    public void setDisponibilities(String day, boolean isAvailable){
        disponibilities.put(day, isAvailable);
    }


}
