package com.example.bicoccahelp.data;

public interface Callback<Data> {
    void onSucces(Data data);

    void onFailure(Exception e);

}
