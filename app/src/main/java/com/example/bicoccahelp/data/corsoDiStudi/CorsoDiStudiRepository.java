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


}
