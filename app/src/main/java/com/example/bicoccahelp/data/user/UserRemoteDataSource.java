package com.example.bicoccahelp.data.user;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import kotlin.reflect.KCallable;

public class UserRemoteDataSource {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    public @Nullable UserModel getCurrentUser(){
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            return null;
        }

        return new UserModel(user.getUid(), user.getEmail(), user.isEmailVerified(), user.getDisplayName(), user.getPhotoUrl());
    }



    public void sendEmailVerification(Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
          callback.onFailure(null) ;
          return;
        }

        user.sendEmailVerification()
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }

    public void reload(Callback<UserModel> callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onFailure(null);
            return;
        }

        user.reload()
                .addOnSuccessListener(command -> callback.onSucces(getCurrentUser()))
                .addOnFailureListener(callback::onFailure);
    }

    /*public void refreshIdToken(Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            callback.onFailure(null);
            return;
        }

        user.getIdToken(true)
                .addOnSuccessListener(command -> callback.onSucces(null))
                .addOnFailureListener(callback::onFailure);
    }*/

    public void updateUsername(String name, Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            callback.onFailure(null);
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnSuccessListener(callback:: onSucces)
                .addOnFailureListener(callback::onFailure);

    }

    public void updatePhoto(Uri photoUri, Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            callback.onFailure(null);
            return;
        }

        if(photoUri == null){
            callback.onFailure(null);
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }

    public void deleteUser(Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            callback.onFailure(null);
            return;
        }

        user.delete()
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }

    public void reauthenticate(String password, Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            callback.onFailure(null);
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), password);

        user.reauthenticate(credential)
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);


    }

    public void updatePassword(String password, Callback<Void> callback) {
        FirebaseUser user = auth.getCurrentUser();

        if(user == null){
            callback.onFailure(null);
            return;
        }

        user.updatePassword(password)
                .addOnSuccessListener(callback:: onSucces)
                .addOnFailureListener(callback::onFailure);
    }
}
