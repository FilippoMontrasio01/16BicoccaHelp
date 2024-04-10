package com.example.bicoccahelp.data.user;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    private final UserRemoteDataSource userRemoteDataSource;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();

    public UserRepository(UserRemoteDataSource userRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
    }

    public void sendEmailVerification(Callback<Void> callback){
        userRemoteDataSource.sendEmailVerification(callback);
    }

    public void reload(Callback<UserModel> callback){

        userRemoteDataSource.reload(callback);
    }

    public void refreshIdToken(Callback<Void> callback){
        userRemoteDataSource.refreshIdToken(callback);
    }

    public void updateUsername(String name, Callback<Void> callback){
        userRemoteDataSource.updateUsername(name, callback);
    }

    public UserModel getCurrentUser(){
        return userRemoteDataSource.getCurrentUser();
    }

    public void deleteUser(Callback<Void> callback){
        userRemoteDataSource.deleteUser(callback);
    }

    public void reauthenticate(String password, Callback<Void> callback){
        userRemoteDataSource.reauthenticate(password, callback);
    }

    public void updatePassword(String password, Callback<Void> callback){
        userRemoteDataSource.updatePassword(password, callback);
    }



}
