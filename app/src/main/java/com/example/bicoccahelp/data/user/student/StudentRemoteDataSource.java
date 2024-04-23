package com.example.bicoccahelp.data.user.student;

import static android.provider.Settings.System.getString;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
                    }else{
                        callback.onSucces(false);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void isTutor(String uid, boolean isTutor, Callback<Boolean> callback){
        students.whereEqualTo(FieldPath.documentId(), uid)
                .whereEqualTo(IS_TUTOR, isTutor)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        callback.onSucces(true);
                    }else{
                        callback.onSucces(false);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void updateStudentName(String uid, String name){
        students.document(uid)
                .update(NAME, name)
                .addOnSuccessListener(aVoid -> Log.d("",
                        "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("",
                        "Error updating document", e));
    }


    public void deleteStudent(String uid){
        students.document(uid)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("",
                        "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w("",
                        "Error deleting document", e));
    }

}
