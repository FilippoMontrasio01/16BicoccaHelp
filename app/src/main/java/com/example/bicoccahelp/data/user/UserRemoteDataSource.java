package com.example.bicoccahelp.data.user;

import androidx.annotation.Nullable;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserRemoteDataSource {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private @Nullable UserModel getCurrentUser(){
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            return null;
        }

        return new UserModel(user.getUid(), user.getEmail(), user.isEmailVerified(), user.getDisplayName());
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

    public void refreshIdToken(Callback<Void> callback){
        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            callback.onFailure(null);
            return;
        }

        user.getIdToken(true)
                .addOnSuccessListener(command -> callback.onSucces(null))
                .addOnFailureListener(callback::onFailure);
    }

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

}
