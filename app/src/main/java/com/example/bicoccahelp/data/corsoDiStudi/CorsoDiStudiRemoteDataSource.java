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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference corsoDiStudi = db.collection("corso di Studi");


    public void createCorso(CreateCorsoDiStudiRequest request, Callback<CorsoDiStudiModel> callback){
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
}
