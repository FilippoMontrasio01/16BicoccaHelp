package com.example.bicoccahelp.data.auth;

import com.example.bicoccahelp.data.Callback;

public class AuthRepository {
    private final AuthRemoteDataSource authRemoteDataSource;


    public AuthRepository(AuthRemoteDataSource authRemoteDataSource) {
        this.authRemoteDataSource = authRemoteDataSource;
    }

    public void register(String email, String password, Callback<Void> callback){
        authRemoteDataSource.register(email, password, callback);
    }

    public void login(String email, String password, Callback<Void> callback){
        authRemoteDataSource.login(email, password, callback);
    }

    public void forgotPassword(String email, Callback<Void> callback){
        authRemoteDataSource.forgotPassword(email, callback);
    }

    public void logout(){
        authRemoteDataSource.logout();
    }

    public void sendEmailVerification(Callback<Void> callback){
        authRemoteDataSource.sendEmailVerification(callback);
    }

}
