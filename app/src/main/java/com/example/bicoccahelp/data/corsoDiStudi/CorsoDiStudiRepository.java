package com.example.bicoccahelp.data.corsoDiStudi;

import com.example.bicoccahelp.data.Callback;

public class CorsoDiStudiRepository {

    private final CorsoDiStudiRemoteDataSource corsoDiStudiRemoteDataSource;


    public CorsoDiStudiRepository(CorsoDiStudiRemoteDataSource corsoDiStudiRemoteDataSource) {
        this.corsoDiStudiRemoteDataSource = corsoDiStudiRemoteDataSource;
    }

    public void createCorso(CreateCorsoDiStudiRequest request,
                            Callback<CorsoDiStudiModel> callback){

        corsoDiStudiRemoteDataSource.createCorso(request, callback);
    }


    public void corsoDiStudiExists(String nomeCorso, Callback<Boolean> callback){
        corsoDiStudiRemoteDataSource.corsoDiStudiExists(nomeCorso, callback);
    }

    public void getCorsoDiStudiIdByName(String nomeCorso, String livello,
                                        Callback<String> callback){

        corsoDiStudiRemoteDataSource.getCorsoDiStudiId(nomeCorso, livello, callback);
    }

    public void getCorsodiStudiName(String idCorso, Callback<String> callback){
        corsoDiStudiRemoteDataSource.getCorsodiStudiName(idCorso, callback);
    }

    public void getCorsoDiStudiLivello(String idCorso, Callback<String> callback){
        corsoDiStudiRemoteDataSource.getCorsoDiStudiLivello(idCorso, callback);
    }

    public void getArea(String idCorso, Callback<String> callback){
        corsoDiStudiRemoteDataSource.getArea(idCorso, callback);
    }


}
