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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateRemoteDataSource {
    private static final String FIELD_DATA = "date";
    private static final String FIELD_ORARI = "disponibilità orario";
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

        orarioTutor.add(data)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    DateModel date = new DateModel(createDateRequest.getDisponibilitaOrari(),
                            createDateRequest.getData(), createDateRequest.getUidTutor());
                    callback.onSucces(date);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void listOrari(String uidTutor, Timestamp data, Long limit, Callback<List<String>> callback) {
        Query query = orarioTutor.whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .whereEqualTo(FIELD_DATA, data)
                .limit(limit);
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    if (documents.size() > 0) {
                        DocumentSnapshot document = documents.get(0);  // Prendiamo il primo documento che corrisponde
                        Map<String, Boolean> disponibilitaOrari = (Map<String, Boolean>) document.get(FIELD_ORARI);
                        if (disponibilitaOrari == null) {
                            disponibilitaOrari = new HashMap<>();
                        }

                        // Creiamo una lista per contenere solo gli orari disponibili
                        List<String> orariDisponibili = new ArrayList<>();
                        for (Map.Entry<String, Boolean> entry : disponibilitaOrari.entrySet()) {
                            if (entry.getValue()) {
                                orariDisponibili.add(entry.getKey().toString());
                            }
                        }

                        Collections.sort(orariDisponibili);

                        callback.onSucces(orariDisponibili);
                    } else {
                        Map<String, Boolean> disponibilitaOrari = new HashMap<>();


                        CreateDateRequest createDateRequest = new CreateDateRequest(disponibilitaOrari, data, uidTutor);
                        createDate(createDateRequest, new Callback<DateModel>() {
                            @Override
                            public void onSucces(DateModel dateModel) {
                                dateModel = new DateModel(disponibilitaOrari, data, uidTutor);
                                onSucces(dateModel);
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void updateDate(Timestamp date, String idDate, Callback<Void> callback) {
        // Costruisci la mappa dei dati da aggiornare nel documento
        Map<String, Object> data = new HashMap<>();
        data.put(FIELD_DATA, date);

        // Esegui l'aggiornamento nel database
        orarioTutor.document(idDate)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    // Operazione di aggiornamento completata con successo
                   callback.onSucces(null);
                })
                .addOnFailureListener(callback::onFailure);
    }


}
