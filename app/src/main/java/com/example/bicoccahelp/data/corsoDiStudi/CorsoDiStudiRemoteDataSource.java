package com.example.bicoccahelp.data.corsoDiStudi;

import android.nfc.Tag;
import android.util.Log;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CorsoDiStudiRemoteDataSource {


    private static final String NOME_CORSO = "nome";
    private static final String AREA = "area di studi";
    private static final String LIVELLO = "tipo di laurea";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference corsoDiStudi = db.collection("Corso di Studi");


    public void createCorso(CreateCorsoDiStudiRequest request,
                            Callback<CorsoDiStudiModel> callback){

        Map<String, Object> data = new HashMap<>();

        data.put(NOME_CORSO, request.getNomeCorso());
        data.put(AREA, request.getArea());
        data.put(LIVELLO, request.getLivello());

        corsoDiStudi.add(data)
                .addOnSuccessListener(documentReference -> {
                    CorsoDiStudiModel corsoDiStudiModel = new CorsoDiStudiModel(
                            corsoDiStudi.getId(), request.getNomeCorso(),
                            request.getArea(), request.getLivello()
                    );
                    callback.onSucces(corsoDiStudiModel);
                })
                .addOnFailureListener(callback::onFailure);

    }


    public void corsoDiStudiExists(String nomeCorso, Callback<Boolean> callback) {
        String nomeCorsoFormatted = nomeCorso.toLowerCase();
        corsoDiStudi.whereEqualTo(NOME_CORSO, nomeCorsoFormatted)
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

    public void getCorsodiStudiName(String idCorso, Callback<String> callback){
        corsoDiStudi.whereEqualTo(FieldPath.documentId(), idCorso)
                .get()
                .addOnSuccessListener(task -> {

                    if (!task.getDocuments().isEmpty()) {
                        DocumentSnapshot documentSnapshot = task.getDocuments().get(0);
                        String nome = documentSnapshot.getString(NOME_CORSO);
                        callback.onSucces(nome);
                    }else{
                        callback.onSucces(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getCorsoDiStudiLivello(String idCorso, Callback<String> callback){
        corsoDiStudi.whereEqualTo(FieldPath.documentId(), idCorso)
                .get()
                .addOnSuccessListener(task -> {
                    if(!task.getDocuments().isEmpty()){
                        DocumentSnapshot documentSnapshot = task.getDocuments().get(0);
                        String livello = documentSnapshot.getString(LIVELLO);
                        callback.onSucces(livello);
                    }else {
                        callback.onSucces(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getArea(String idCorso, Callback<String> callback){
        corsoDiStudi.whereEqualTo(FieldPath.documentId(), idCorso)
                .get()
                .addOnSuccessListener(task -> {
                    if(!task.getDocuments().isEmpty()){
                        DocumentSnapshot documentSnapshot = task.getDocuments().get(0);
                        String areaStudi = documentSnapshot.getString(AREA);
                        callback.onSucces(areaStudi);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getCorsoId(String nomeCorso, Callback<String> callback) {
        String nomeCorsoFormatted = nomeCorso.toLowerCase();
        corsoDiStudi.whereEqualTo(NOME_CORSO, nomeCorsoFormatted)
                .get()
                .addOnSuccessListener(task -> {
                    if (!task.getDocuments().isEmpty()) {
                        String id = task.getDocuments().get(0).getId();
                        callback.onSucces(id);
                    } else {
                        callback.onSucces(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }


    public void getCorsoDiStudiIdByName(String nomeCorso, String livello, Callback<String> callback) {
        String nomeCorsoFormatted = nomeCorso.toLowerCase();
        corsoDiStudi.whereEqualTo(NOME_CORSO, nomeCorsoFormatted)
                .whereEqualTo(LIVELLO, livello)
                .get()
                .addOnSuccessListener(task -> {
                    if (!task.getDocuments().isEmpty()) {
                        String id = task.getDocuments().get(0).getId();
                        callback.onSucces(id);
                    } else {
                        callback.onSucces(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
}
