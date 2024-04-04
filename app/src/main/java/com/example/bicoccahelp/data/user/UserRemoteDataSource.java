package com.example.bicoccahelp.data.user;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRemoteDataSource {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();



    public void sendEmailVerification(Callback<Void> callback){
        if(user == null){
          callback.onFailure(null) ;
          return;
        }

        user.sendEmailVerification()
                .addOnSuccessListener(callback::onSucces)
                .addOnFailureListener(callback::onFailure);
    }
}
