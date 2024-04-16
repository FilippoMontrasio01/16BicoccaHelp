package com.example.bicoccahelp.data.user;

import android.net.Uri;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;

public class UserRepository {

    private final UserRemoteDataSource userRemoteDataSource;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final UserAssetsRemoteDataSource userAssetsRemoteDataSource;

    public UserRepository(UserRemoteDataSource userRemoteDataSource, UserAssetsRemoteDataSource userAssetsRemoteDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userAssetsRemoteDataSource = userAssetsRemoteDataSource;
    }

    public void sendEmailVerification(Callback<Void> callback){
        userRemoteDataSource.sendEmailVerification(callback);
    }

    public void reload(Callback<UserModel> callback){

        userRemoteDataSource.reload(callback);
    }

    /*public void refreshIdToken(Callback<Void> callback){
        userRemoteDataSource.refreshIdToken(callback);
    }*/

    public void updateUsername(String name, Callback<Void> callback){
        userRemoteDataSource.updateUsername(name, callback);
    }

    public void updatePhoto(Uri photoUri, Callback<Void> callback){

        UserModel user = userRemoteDataSource.getCurrentUser();

        if(user == null){
            callback.onFailure(null);
            return;
        }

        String userPhotoPath = "user/" + user.uid + "/profile_photo.jpeg";
        userAssetsRemoteDataSource.upload(userPhotoPath, photoUri, new Callback<String>() {
            @Override
            public void onSucces(String photoPath) {
                userRemoteDataSource.updatePhoto(Uri.parse(photoPath), callback);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
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
