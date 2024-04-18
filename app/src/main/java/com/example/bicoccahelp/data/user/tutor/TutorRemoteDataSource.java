package com.example.bicoccahelp.data.user.tutor;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TutorRemoteDataSource {

    private static final String UID = "uid";
    private static final String FIELD_EMAIL = "email";
    private static final String NAME = "nome";
    private static final String PHOTO_URI = "photoUri";
    private static final String CORSO_DI_STUDI = "corso di Studi";
    private static final String DISPONIBILITA_GIORNI = "disponibilità Giorni";
    private static final String DISPONIBILITA_ORE = "disponibilità Ore";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference tutors = db.collection("tutor");

    public void createTutor(CreateTutorRequest createTutorRequest, Callback<TutorModel> callback){
        Map<String, Object> data = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            callback.onFailure(new Exception("Error"));
            return;
        }

        data.put(UID, user.getUid());
        data.put(FIELD_EMAIL, user.getEmail());
        data.put(NAME, user.getDisplayName());
        data.put(PHOTO_URI, user.getPhotoUrl());
        data.put(CORSO_DI_STUDI, createTutorRequest.corsoDiStudi);
        data.put(DISPONIBILITA_GIORNI, createTutorRequest.disponibilitaGiorni);
        data.put(DISPONIBILITA_ORE, createTutorRequest.disponibilitaOre);

        tutors.add(data)
                .addOnSuccessListener(documentReference -> {
                    TutorModel tutor = new TutorModel(user.getUid(),
                            user.getEmail(),user.isEmailVerified(),
                            user.getDisplayName(),
                            user.getPhotoUrl(), createTutorRequest.disponibilitaGiorni,
                            createTutorRequest.disponibilitaOre, createTutorRequest.corsoDiStudi,
                            tutors.getId());
                    callback.onSucces(tutor);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
