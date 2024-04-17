package com.example.bicoccahelp.data.user.student;

import android.net.Uri;

import androidx.annotation.NonNull;

public class CreateStudentRequest {

    public final @NonNull String uid;

    public final @NonNull String email;
    public final @NonNull boolean emailVerified;
    public final @NonNull String name;
    public final @NonNull Uri photoUri;
    public final @NonNull String corsoDiStudi;


    public CreateStudentRequest(@NonNull String uid, @NonNull String email, boolean emailVerified, @NonNull String name, @NonNull Uri photoUri, @NonNull String corsoDiStudi) {
        this.uid = uid;
        this.email = email;
        this.emailVerified = emailVerified;
        this.name = name;
        this.photoUri = photoUri;
        this.corsoDiStudi = corsoDiStudi;
    }
}
