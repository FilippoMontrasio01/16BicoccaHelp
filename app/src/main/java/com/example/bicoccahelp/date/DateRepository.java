package com.example.bicoccahelp.date;

import com.example.bicoccahelp.data.Callback;
import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.List;
import java.util.Map;

public class DateRepository {
    private DateRemoteDataSource dateRemoteDataSource;

    public DateRepository(DateRemoteDataSource dateRemoteDataSource) {
        this.dateRemoteDataSource = dateRemoteDataSource;
    }

    public void createDate(CreateDateRequest createDateRequest, Callback<DateModel> callback){
        dateRemoteDataSource.createDate(createDateRequest, callback);
    }

    public void listOrari(String uidTutor, String uidStudent, Timestamp data, Long limit, Callback<List<String>> callback){
        dateRemoteDataSource.listOrari(uidTutor, uidStudent, data,limit, callback);
    }

    public void updateDate(String uidTutor, String uidStudent, Timestamp date, Timestamp nuovaData, Callback<Void> callback){
        dateRemoteDataSource.updateDate(uidTutor, uidStudent, date, nuovaData, callback);
    }
}
