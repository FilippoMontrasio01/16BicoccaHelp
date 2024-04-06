package com.example.bicoccahelp.data.user;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRemoteDataSource {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();




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

    public void reload(Callback<Void> callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onFailure(null);
            return;
        }

        user.reload()
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }

}
