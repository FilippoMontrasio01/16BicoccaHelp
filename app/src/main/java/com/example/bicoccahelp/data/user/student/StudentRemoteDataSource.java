package com.example.bicoccahelp.data.user.student;

import static android.provider.Settings.System.getString;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentRemoteDataSource {
    private static final String FIELD_EMAIL = "email";
    private static final String NAME = "name";
    private static final String PHOTO_URI = "photoUri";
    private static final String CORSO_DI_STUDI = "corsoDiStudi";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference students = db.collection("student");

    public void createStudent(CreateStudentRequest createStudentRequest, Callback<StudentModel> callback){
        Map<String, Object> data = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            callback.onFailure(new Exception("Error"));
            return;
        }


        data.put(FIELD_EMAIL, createStudentRequest.email);
        data.put(NAME, createStudentRequest.name);
        data.put(PHOTO_URI, createStudentRequest.photoUri);
        data.put(CORSO_DI_STUDI, createStudentRequest.corsoDiStudi);



        students.document(user.getUid())
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    StudentModel student = new StudentModel(createStudentRequest.uid,
                            createStudentRequest.email, createStudentRequest.emailVerified,
                            createStudentRequest.name, createStudentRequest.photoUri,
                            createStudentRequest.corsoDiStudi);
                    callback.onSucces(student);
                })
                .addOnFailureListener(callback::onFailure);
    }


}
