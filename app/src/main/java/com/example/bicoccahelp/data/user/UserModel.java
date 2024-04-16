package com.example.bicoccahelp.data.user;

import android.net.Uri;

public class UserModel {
    public final String uid;
    public final String email;
    public boolean emailVerified;
    public final String name;
    public final Uri photoUri;


    public UserModel(String uid, String email, boolean emailVerified, String name, Uri photoUri) {
        this.uid = uid;
        this.email = email;
        this.emailVerified = emailVerified;
        this.name = name;
        this.photoUri = photoUri;
    }

}

