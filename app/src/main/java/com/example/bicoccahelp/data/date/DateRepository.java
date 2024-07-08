package com.example.bicoccahelp.data.date;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.Timestamp;

import java.util.List;

public class DateRepository {
    private DateRemoteDataSource dateRemoteDataSource;

    public DateRepository(DateRemoteDataSource dateRemoteDataSource) {
        this.dateRemoteDataSource = dateRemoteDataSource;
    }

    public void createDate(CreateDateRequest createDateRequest, Callback<DateModel> callback){
        dateRemoteDataSource.createDate(createDateRequest, callback);
    }

    public void listOrari(String uidTutor,Timestamp data, Long limit, Callback<List<String>> callback){
        dateRemoteDataSource.listOrari(uidTutor,data,limit, callback);
    }

    public void updateDate(String uidTutor, Timestamp date, Timestamp nuovaData, Callback<Void> callback){
        dateRemoteDataSource.updateDate(uidTutor,date, nuovaData, callback);
    }
}
