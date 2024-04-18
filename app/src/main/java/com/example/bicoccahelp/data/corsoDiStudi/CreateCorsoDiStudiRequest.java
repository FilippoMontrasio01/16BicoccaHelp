package com.example.bicoccahelp.data.corsoDiStudi;

import androidx.annotation.NonNull;

public class CreateCorsoDiStudiRequest {
    public @NonNull String nomeCorso;
    public @NonNull String area;
    public @NonNull String livello;

    public CreateCorsoDiStudiRequest(@NonNull String nomeCorso,
                                     @NonNull String area, @NonNull String livello){

        this.nomeCorso = nomeCorso;
        this.area = area;
        this.livello = livello;
    }

}
