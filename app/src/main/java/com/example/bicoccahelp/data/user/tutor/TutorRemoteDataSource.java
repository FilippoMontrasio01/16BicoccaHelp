package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;
import android.util.Log;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.guieffect.qual.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorRemoteDataSource {

    private static final String UID = "uid studente";
    private static final String FIELD_EMAIL = "email";
    private static final String NAME = "nome";
    private static final String PHOTO_URI = "photoUri";
    private static final String EMAIL_VERIFIED = "emailVerified";
    private static final String CORSO_DI_STUDI = "corso di studi";
    private static final String DISPONIBILITA_GIORNI = "disponibilità giorni";
    private static final String SKILLS = "skills";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference tutors = db.collection("Tutor");
    private DocumentSnapshot lastDocument;

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
        data.put(SKILLS, createTutorRequest.skills);

        tutors.document(user.getUid())
                .set(data)
                .addOnSuccessListener(documentReference -> {
                    TutorModel tutor = new TutorModel(user.getUid(),
                            user.getEmail(),user.isEmailVerified(),
                            user.getDisplayName(),
                            user.getPhotoUrl(), createTutorRequest.disponibilitaGiorni,
                            createTutorRequest.corsoDiStudi,
                            createTutorRequest.skills);
                    callback.onSucces(tutor);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void listTutors(Long limit, Callback<List<TutorModel>> callback) {
        Query query = tutors.orderBy(NAME, Query.Direction.ASCENDING).limit(limit);
        if (lastDocument != null) {
            query = query.startAfter(lastDocument);
        }
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    if (documents.size() > 0) {
                        lastDocument = documents.get(documents.size() - 1);
                    }
                    List<TutorModel> tutorList = new ArrayList<>();

                    for (DocumentSnapshot document: documents) {
                        String id = document.getId() != null ? document.getId() : "";
                        Log.d("", "ID USER: "+ id);
                        String email = document.getString(FIELD_EMAIL) != null ? document.getString(FIELD_EMAIL) : "";
                        Log.d("", "EMAIL USER: "+ email);
                        Boolean emailVerified = document.getBoolean(EMAIL_VERIFIED) != null ? document.getBoolean(EMAIL_VERIFIED) : false;
                        String name = document.getString(NAME) != null ? document.getString(NAME) : "";
                        Log.d("", "NAME USER: "+ name);
                        String photoUriString = document.getString(PHOTO_URI);
                        if (photoUriString == null) {
                            photoUriString = "";
                        }
                        Uri photoUri = Uri.parse(photoUriString);
                        Map<String, Boolean> disponibilitaGiorni = (Map<String, Boolean>) document.get(DISPONIBILITA_GIORNI);
                        if (disponibilitaGiorni == null) {
                            disponibilitaGiorni = new HashMap<>();
                        }
                        String corsoDiStudi = document.getString(CORSO_DI_STUDI) != null ? document.getString(CORSO_DI_STUDI) : "";
                        ArrayList<String> skills = (ArrayList<String>) document.get(SKILLS);
                        if (skills == null) {
                            skills = new ArrayList<>();
                        }

                        TutorModel tutorModel = new TutorModel(
                                id,
                                email,
                                emailVerified,
                                name,
                                photoUri,
                                disponibilitaGiorni,
                                corsoDiStudi,
                                skills);
                        tutorList.add(tutorModel);
                    }

                    callback.onSucces(tutorList);
                })
                .addOnFailureListener(callback::onFailure);

    }

    public void listTutorName(String name, Long limit, Callback<List<TutorModel>> callback){

        String endOfName = name + "\uf8ff";

        Query query = tutors.whereGreaterThanOrEqualTo(NAME, name)
                .whereLessThanOrEqualTo(NAME, endOfName)
                .orderBy(NAME, Query.Direction.ASCENDING)
                .limit(limit);


        Log.d("", "NAME USER: "+ name);




        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    if (documents.size() > 0) {
                        lastDocument = documents.get(documents.size() - 1);
                    }
                    List<TutorModel> tutorList = new ArrayList<>();

                    for (DocumentSnapshot document: documents) {
                        String id = document.getId() != null ? document.getId() : "";
                        Log.d("", "ID USER: "+ id);
                        String email = document.getString(FIELD_EMAIL) != null ? document.getString(FIELD_EMAIL) : "";
                        Log.d("", "EMAIL USER: "+ email);
                        Boolean emailVerified = document.getBoolean(EMAIL_VERIFIED) != null ? document.getBoolean(EMAIL_VERIFIED) : false;
                        String tutorName = document.getString(NAME) != null ? document.getString(NAME) : "";
                        Log.d("", "NAME USER: "+ name);
                        String photoUriString = document.getString(PHOTO_URI);
                        if (photoUriString == null) {
                            photoUriString = "";
                        }
                        Uri photoUri = Uri.parse(photoUriString);
                        Map<String, Boolean> disponibilitaGiorni = (Map<String, Boolean>) document.get(DISPONIBILITA_GIORNI);
                        if (disponibilitaGiorni == null) {
                            disponibilitaGiorni = new HashMap<>();
                        }
                        String corsoDiStudi = document.getString(CORSO_DI_STUDI) != null ? document.getString(CORSO_DI_STUDI) : "";
                        ArrayList<String> skills = (ArrayList<String>) document.get(SKILLS);
                        if (skills == null) {
                            skills = new ArrayList<>();
                        }

                        TutorModel tutorModel = new TutorModel(
                                id,
                                email,
                                emailVerified,
                                tutorName,
                                photoUri,
                                disponibilitaGiorni,
                                corsoDiStudi,
                                skills);

                        tutorList.add(tutorModel);


                    }

                    callback.onSucces(tutorList);
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
                            createTutorRequest.corsoDiStudi,
                            createTutorRequest.skills);
                    callback.onSucces(tutor);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void tutorExist(String uid, Callback<Boolean> callback){
        tutors.whereEqualTo(FieldPath.documentId(), uid)
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

    public void updateTutorPhoto(String uid, Uri photoUri){
        tutors.document(uid)
                .update(PHOTO_URI, photoUri)
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
