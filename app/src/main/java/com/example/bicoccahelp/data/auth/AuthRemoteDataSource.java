package com.example.bicoccahelp.data.auth;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRemoteDataSource {
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    public void register(String email, String password, Callback<Void> callback){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.onSucces(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void login(String email, String password, Callback<Void> callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> callback.onSucces(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void forgotPassword(String email, Callback<Void> callback){
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }

    public void logout(){
        auth.signOut();
    }
}
