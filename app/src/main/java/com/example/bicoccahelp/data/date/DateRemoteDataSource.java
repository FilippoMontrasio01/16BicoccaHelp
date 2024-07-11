package com.example.bicoccahelp.data.date;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
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
                    DateModel date = new DateModel(createDateRequest.getDisponibilitaOrari(),
                            createDateRequest.getData(), createDateRequest.getUidTutor());
                    callback.onSucces(date);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void listOrari(String uidTutor, Timestamp giorno, Long limit, Callback<List<String>> callback) {
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
                        CreateDateRequest createDateRequest = new CreateDateRequest(new HashMap<>(), finalGiorno, uidTutor);

                        // Chiamiamo createDate in modo sincrono
                        createDate(createDateRequest, new Callback<DateModel>() {
                            @Override
                            public void onSucces(DateModel dateModel) {
                                // Dopo aver creato la data, otteniamo gli orari disponibili
                                fetchAvailableTimes(uidTutor, finalGiorno, callback);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                callback.onFailure(e);
                            }
                        });
                    } else {
                        // Se ci sono documenti, otteniamo gli orari disponibili direttamente
                        fetchAvailableTimes(uidTutor, finalGiorno, callback);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    private void fetchAvailableTimes(String uidTutor, Timestamp giorno, Callback<List<String>> callback) {
        orarioTutor.whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .whereEqualTo(FIELD_DATA, giorno)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0); // Prendiamo il primo documento che corrisponde
                        Map<String, Boolean> disponibilitaOrari = document.get(FIELD_ORARI) != null ? (Map<String, Boolean>) document.get(FIELD_ORARI) : new HashMap<>();

                        // Creiamo una lista per contenere solo gli orari disponibili
                        List<String> orariDisponibili = new ArrayList<>();
                        for (Map.Entry<String, Boolean> entry : disponibilitaOrari.entrySet()) {
                            if (entry.getValue()) {
                                orariDisponibili.add(entry.getKey());
                            }
                        }

                        Collections.sort(orariDisponibili);
                        callback.onSucces(orariDisponibili);
                    } else {
                        callback.onFailure(new Exception("No matching document found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void updateDate(String uidTutor, Timestamp date, Timestamp nuovaData, Callback<Void> callback) {
        orarioTutor
                .whereEqualTo(FIELD_UID_TUTOR, uidTutor)
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

    public void updateOrario(String uidTutor, Timestamp date, String orario, Callback<Void> callback) {
        orarioTutor
                .whereEqualTo(FIELD_UID_TUTOR, uidTutor)
                .whereEqualTo(FIELD_DATA, date)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference documentReference = documentSnapshot.getReference();

                        // Aggiorna il campo specifico nella mappa degli orari
                        documentReference.update(FIELD_ORARI + "." + orario, false)
                                .addOnSuccessListener(aVoid -> {
                                    callback.onSucces(null);
                                })
                                .addOnFailureListener(e -> {
                                    callback.onFailure(e);
                                });
                    } else {
                        callback.onFailure(new Exception("No matching document found"));
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e);
                });
    }


}
