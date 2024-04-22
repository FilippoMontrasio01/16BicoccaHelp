package com.example.bicoccahelp.data.user.student;

import static android.provider.Settings.System.getString;

import android.util.Log;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentRemoteDataSource {
    private static final String FIELD_EMAIL = "email";
    private static final String NAME = "nome";
    private static final String PHOTO_URI = "photoUri";
    private static final String CORSO_DI_STUDI = "corso Di Studi";
    private static final String IS_TUTOR = "isTutor";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference students = db.collection("Student");

    public void createStudent(CreateStudentRequest createStudentRequest,
                              Callback<StudentModel> callback){
        Map<String, Object> data = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            callback.onFailure(new Exception("Error"));
            return;
        }


        data.put(FIELD_EMAIL, user.getEmail());
        data.put(NAME, user.getDisplayName());
        data.put(PHOTO_URI, user.getPhotoUrl());
        data.put(CORSO_DI_STUDI, createStudentRequest.corsoDiStudi);
        data.put(IS_TUTOR, createStudentRequest.isTutor);



        students.document(user.getUid())
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    StudentModel student = new StudentModel(user.getUid(),
                           user.getEmail(), user.isEmailVerified(),
                            user.getDisplayName(), user.getPhotoUrl(),
                            createStudentRequest.corsoDiStudi,
                            createStudentRequest.isTutor);
                    callback.onSucces(student);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void studentExist(String uid, Callback<Boolean> callback){
        students.whereEqualTo(FieldPath.documentId(), uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        callback.onSucces(true);
                        Log.d("ciao", "UID: "+uid + " STUDENT ID: "+ students.getId());
                    }else{
                        callback.onSucces(false);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


}
