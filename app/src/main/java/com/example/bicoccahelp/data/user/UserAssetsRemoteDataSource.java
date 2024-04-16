package com.example.bicoccahelp.data.user;

import android.net.Uri;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserAssetsRemoteDataSource {

    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public void upload(String assethPath, Uri assetUri, Callback<String> callback){
        StorageReference reference = storageReference.child(assethPath);
        reference.putFile(assetUri)
                .addOnSuccessListener(res -> callback.onSucces(assethPath))
                .addOnFailureListener(callback::onFailure);
    }

}
