package com.example.bicoccahelp.data.user.student;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

public class StudentModel extends UserModel {

    private final @NonNull String corsoDiStudi;
    private final boolean isTutor;

    public StudentModel(String uid, String email, boolean emailVerified, String name,
                        Uri photoUri, @NonNull String corsoDiStudi,
                        boolean isTutor) {
        super(uid, email, emailVerified, name, photoUri);
        this.corsoDiStudi = corsoDiStudi;
        this.isTutor = isTutor;
    }

    @NonNull
    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }

    public boolean isTutor() {
        return isTutor;
    }




}
