package com.example.bicoccahelp.data.corsoDiStudi;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CorsoDiStudiRemoteDataSource {


    private static final String NOME_CORSO = "nome";
    private static final String AREA = "area di studi";
    private static final String LIVELLO = "tipo di laurea";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference corsoDiStudi = db.collection("Corso Di Studi");


    public void createCorso(CreateCorsoDiStudiRequest request,
                            Callback<CorsoDiStudiModel> callback){

        Map<String, Object> data = new HashMap<>();

        data.put(NOME_CORSO, request.nomeCorso);
        data.put(AREA, request.area);
        data.put(LIVELLO, request.livello);

        corsoDiStudi.add(data)
                .addOnSuccessListener(documentReference -> {
                    CorsoDiStudiModel corsoDiStudiModel = new CorsoDiStudiModel(
                            corsoDiStudi.getId(), request.nomeCorso,
                            request.area, request.livello
                    );
                    callback.onSucces(corsoDiStudiModel);
                })
                .addOnFailureListener(callback::onFailure);

    }


    public void corsoDiStudiExists(String nomeCorso, Callback<Boolean> callback) {
        //String nomeCorsoFormatted = nomeCorso.trim().toLowerCase();

        corsoDiStudi.whereEqualTo(NOME_CORSO, nomeCorso)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean exists = !queryDocumentSnapshots.isEmpty();
                    callback.onSucces(exists);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getCorsoDiStudiIdByName(String nomeCorso, Callback<String> callback) {
        corsoDiStudi.whereEqualTo(NOME_CORSO, nomeCorso)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String corsoId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        callback.onSucces(corsoId);
                    } else {

                        callback.onFailure(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
}
