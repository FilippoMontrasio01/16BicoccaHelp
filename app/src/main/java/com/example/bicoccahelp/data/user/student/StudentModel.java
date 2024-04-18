package com.example.bicoccahelp.data.user.student;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.data.user.UserModel;

import org.checkerframework.checker.units.qual.N;

public class StudentModel extends UserModel {

    public final @NonNull String corsoDiStudi;
    public final @NonNull boolean isTutor;

    public StudentModel(String uid, String email, boolean emailVerified, String name,
                        Uri photoUri, @NonNull String corsoDiStudi,
                        @NonNull boolean isTutor) {
        super(uid, email, emailVerified, name, photoUri);
        this.corsoDiStudi = corsoDiStudi;
        this.isTutor = isTutor;
    }
}
