package com.example.bicoccahelp.date;

import android.net.Uri;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.tutor.CreateTutorRequest;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateRemoteDataSource {
    private static final String FIELD_DATA = "date";
    private static final String FIELD_ORARI = "disponibilità orario";
    private static final String FIELD_UID_STUDENT = "uid Student";
    private static final String FIELD_UID_TUTOR = "uid Tutor";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference orarioTutor = db.collection("orarioTutor");
    private DocumentSnapshot lastDocument;

    public void createDate(CreateDateRequest createDateRequest, Callback<DateModel> callback){
        Map<String, Object> data = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            callback.onFailure(new Exception("Error"));
            return;
        }

        Map<String, Boolean> disponibilitaOrari = new HashMap<>();
        for (int i = 10; i <= 18; i++) {
            String ora = String.format("%02d:00", i);
            disponibilitaOrari.put(ora, true);
        }

        data.put(FIELD_DATA, createDateRequest.getData());
        data.put(FIELD_ORARI, disponibilitaOrari);
        data.put(FIELD_UID_TUTOR, createDateRequest.getUidTutor());
        data.put(FIELD_UID_STUDENT, createDateRequest.getUidStudent());

        orarioTutor.add(data)
                .addOnSuccessListener(documentReference -> {
                    DateModel date = new DateModel(createDateRequest.getDisponibilitaOrari(),
                            createDateRequest.getData(), createDateRequest.getUidTutor(),
                            createDateRequest.getUidStudent());
                    callback.onSucces(date);
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void listOrari(String uidTutor, String uidStudent, Timestamp giorno, Long limit, Callback<List<String>> callback) {
        if (giorno == null) {
            // Se la data è nulla, impostiamo la data attuale
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            giorno = new Timestamp(calendar.getTime());
        }

        Timestamp finalGiorno = giorno;

        Query query = orarioTutor.whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .whereEqualTo(FIELD_DATA, finalGiorno)
                .limit(limit);

        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    if (documents.isEmpty()) {
                        // Se non ci sono documenti corrispondenti, creiamo un nuovo documento con il metodo createDate
                        CreateDateRequest createDateRequest = new CreateDateRequest(new HashMap<>(), finalGiorno, uidTutor, uidStudent);


                        createDate(createDateRequest, new Callback<DateModel>() {
                            @Override
                            public void onSucces(DateModel dateModel) {

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    } else {
                        DocumentSnapshot document = documents.get(0); // Prendiamo il primo documento che corrisponde
                        Map<String, Boolean> disponibilitaOrari = document.get(FIELD_ORARI) != null ? (Map<String, Boolean>) document.get(FIELD_ORARI) : new HashMap<>();

                        // Creiamo una lista per contenere solo gli orari disponibili
                        List<String> orariDisponibili = new ArrayList<>();
                        for (Map.Entry<String, Boolean> entry : disponibilitaOrari.entrySet()) {
                            if (entry.getValue()) {
                                orariDisponibili.add(entry.getKey().toString());
                            }
                        }

                        Collections.sort(orariDisponibili);
                        callback.onSucces(orariDisponibili);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void updateDate(String uidTutor, String uidStudent, Timestamp date, Timestamp nuovaData, Callback<Void> callback) {
        orarioTutor
                .whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .whereEqualTo(FIELD_UID_STUDENT, uidStudent)
                .whereEqualTo(FIELD_DATA, date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentId = documentSnapshot.getId();
                        documentSnapshot.getReference().update(FIELD_DATA, nuovaData)
                                .addOnSuccessListener(aVoid -> {

                                })
                                .addOnFailureListener(e -> {

                                });
                    }


                    callback.onSucces(null);
                })
                .addOnFailureListener(e -> {

                });

    }


}
