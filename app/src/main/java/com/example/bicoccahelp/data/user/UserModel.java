package com.example.bicoccahelp.data.user;

import android.net.Uri;

public class UserModel {
    private final String uid;
    private final String email;
    private boolean emailVerified;
    private String name;
    private Uri photoUri;


    public UserModel(String uid, String email, boolean emailVerified, String name, Uri photoUri) {
        this.uid = uid;
        this.email = email;
        this.emailVerified = emailVerified;
        this.name = name;
        this.photoUri = photoUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getName() {
        return name;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }
}

