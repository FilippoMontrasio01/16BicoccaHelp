package com.example.bicoccahelp.utils;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class GlideLoadModel {
    public static StorageReference get(String path){
        return FirebaseStorage.getInstance().getReference(path);
    }
}
