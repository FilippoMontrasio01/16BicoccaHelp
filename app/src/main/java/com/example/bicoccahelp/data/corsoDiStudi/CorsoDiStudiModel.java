package com.example.bicoccahelp.data.corsoDiStudi;

import androidx.annotation.NonNull;

public class CorsoDiStudiModel {
    public @NonNull String idCorso;
    public @NonNull String nomeCorso;
    public @NonNull String area;
    public @NonNull String livello;

    public CorsoDiStudiModel(@NonNull String idCorso,@NonNull String nomeCorso,
                             @NonNull String area, @NonNull String livello){
        this.idCorso = idCorso;
        this.nomeCorso = nomeCorso;
        this.area = area;
        this.livello = livello;
    }
}
