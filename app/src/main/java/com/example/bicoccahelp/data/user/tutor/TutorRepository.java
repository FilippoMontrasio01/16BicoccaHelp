package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;

import com.example.bicoccahelp.data.Callback;

import java.util.List;

public class TutorRepository {
    private final TutorRemoteDataSource tutorRemoteDataSource;

    public TutorRepository(TutorRemoteDataSource tutorRemoteDataSource) {
        this.tutorRemoteDataSource = tutorRemoteDataSource;
    }

    public void createTutor(CreateTutorRequest createTutorRequest, Callback<TutorModel> callback){
        tutorRemoteDataSource.createTutor(createTutorRequest, callback);
    }

    public void deleteTutor(String uid){
        tutorRemoteDataSource.deleteTutor(uid);
    }

    public void tutorExist(String uid, Callback<Boolean> callback){
        tutorRemoteDataSource.tutorExist(uid, callback);
    }



    public void updateTutorName(String uid, String name){
        tutorRemoteDataSource.updateTutorName(uid, name);
    }

    public void listTutors(Long limit, Callback<List<TutorModel>> callback) {
        tutorRemoteDataSource.listTutors(limit, callback);
    }

    public void listTutorName(String name, Long limit, Callback<List<TutorModel>> callback){
        tutorRemoteDataSource.listTutorName(name, limit, callback);
    }

    public void updateTutorPhoto(String uid, Uri photouri){
        tutorRemoteDataSource.updateTutorPhoto(uid, photouri);
    }


    public void getTutorId(String uid, Callback<String> callback){
        tutorRemoteDataSource.getTutorId(uid, callback);
    }
    public void updateTutor(CreateTutorRequest createTutorRequest,
                             String idTutor, Callback<TutorModel> callback){

        tutorRemoteDataSource.updateTutor(createTutorRequest, idTutor, callback);
    }
}
