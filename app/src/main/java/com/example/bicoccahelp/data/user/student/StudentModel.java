package com.example.bicoccahelp.data.user.student;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

public class StudentModel extends UserModel {

    public final @NonNull String corsoDiStudi;

    public StudentModel(String uid, String email, boolean emailVerified, String name, Uri photoUri, @NonNull String corsoDiStudi) {
        super(uid, email, emailVerified, name, photoUri);
        this.corsoDiStudi = corsoDiStudi;
    }
}
