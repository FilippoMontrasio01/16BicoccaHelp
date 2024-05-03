package com.example.bicoccahelp.data.corsoDiStudi;

import androidx.annotation.NonNull;

public class CreateCorsoDiStudiRequest {
    private
    @NonNull String nomeCorso;
    private @NonNull String area;
    private
    @NonNull String livello;

    public CreateCorsoDiStudiRequest(@NonNull String nomeCorso,
                                     @NonNull String area, @NonNull String livello){

        this.nomeCorso = nomeCorso;
        this.area = area;
        this.livello = livello;
    }

    public void setNomeCorso(@NonNull String nomeCorso) {
        this.nomeCorso = nomeCorso;
    }

    public void setArea(@NonNull String area) {
        this.area = area;
    }

    public void setLivello(@NonNull String livello) {
        this.livello = livello;
    }

    @NonNull
    public String getNomeCorso() {
        return nomeCorso;
    }

    @NonNull
    public String getArea() {
        return area;
    }

    @NonNull
    public String getLivello() {
        return livello;
    }
}
