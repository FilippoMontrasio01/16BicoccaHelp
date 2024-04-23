package com.example.bicoccahelp.data.user.tutor;

import android.util.Log;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.guieffect.qual.UI;

import java.util.HashMap;
import java.util.Map;

public class TutorRemoteDataSource {

    private static final String UID = "uid studente";
    private static final String FIELD_EMAIL = "email";
    private static final String NAME = "nome";
    private static final String PHOTO_URI = "photoUri";
    private static final String CORSO_DI_STUDI = "corso Di Studi";
    private static final String DISPONIBILITA_GIORNI = "disponibilità Giorni";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference tutors = db.collection("Tutor");

    public void createTutor(CreateTutorRequest createTutorRequest, Callback<TutorModel> callback){
        Map<String, Object> data = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            callback.onFailure(new Exception("Error"));
            return;
        }

        data.put(FIELD_EMAIL, user.getEmail());
        data.put(NAME, user.getDisplayName());
        data.put(PHOTO_URI, user.getPhotoUrl());
        data.put(CORSO_DI_STUDI, createTutorRequest.corsoDiStudi);
        data.put(DISPONIBILITA_GIORNI, createTutorRequest.disponibilitaGiorni);

        tutors.document(user.getUid())
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    TutorModel tutor = new TutorModel(user.getUid(),
                            user.getEmail(),user.isEmailVerified(),
                            user.getDisplayName(),
                            user.getPhotoUrl(), createTutorRequest.disponibilitaGiorni,
                            createTutorRequest.corsoDiStudi);
                    callback.onSucces(tutor);
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void updateTutor(CreateTutorRequest createTutorRequest, String idTutor,
                            Callback<TutorModel> callback){

        Map<String, Object> data = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            callback.onFailure(new Exception("Error"));
            return;
        }

        data.put(CORSO_DI_STUDI, createTutorRequest.corsoDiStudi);
        data.put(DISPONIBILITA_GIORNI, createTutorRequest.disponibilitaGiorni);

        tutors.document(idTutor)
                .update(data)
                .addOnSuccessListener(documentReference -> {
                    TutorModel tutor = new TutorModel(user.getUid(),
                            user.getEmail(),user.isEmailVerified(),
                            user.getDisplayName(),
                            user.getPhotoUrl(), createTutorRequest.disponibilitaGiorni,
                            createTutorRequest.corsoDiStudi);
                    callback.onSucces(tutor);
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void getTutorId(String uid, Callback<String> callback) {

        tutors.whereEqualTo(UID, uid)
                .get()
                .addOnSuccessListener(task -> {
                    if (!task.getDocuments().isEmpty()) {
                        String id = task.getDocuments().get(0).getId();

                        Log.d("","L'ID E': "+id);
                        callback.onSucces(id);
                    } else {
                        Log.d("","L'ID NON ESISTE");
                        callback.onSucces(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void updateTutorName(String uid, String name){
        tutors.document(uid)
                .update(NAME, name)
                .addOnSuccessListener(aVoid -> Log.d("",
                        "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w("",
                        "Error updating document", e));
    }

    public void deleteTutor(String uid){
        tutors.document(uid)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("",
                        "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w("",
                        "Error deleting document", e));
    }
}
