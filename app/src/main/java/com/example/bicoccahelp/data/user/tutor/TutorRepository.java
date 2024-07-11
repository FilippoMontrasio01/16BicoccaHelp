package com.example.bicoccahelp.data.user.tutor;

import android.net.Uri;
import android.telecom.Call;

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

    public void listTutorSkill(String skill, Long limit, Callback<List<TutorModel>> callback){
        tutorRemoteDataSource.listTutorSkill(skill, limit, callback);
    }

    public void listTutorsCorsodiStudi(String idCorso, Long limit,
                                       Callback<List<TutorModel>> callback){
        tutorRemoteDataSource.listTutorsCorsodiStudi(idCorso, limit, callback);
    }

    public void listTutorDisponibility(String day, Long limit, Callback<List<TutorModel>> callback){
        tutorRemoteDataSource.listTutorDisponibility(day, limit, callback);
    }

    public void getTutorUid(String name, Callback<String> callback) {
        tutorRemoteDataSource.getTutorUid(name, callback);
    }
    public void updateTutor(CreateTutorRequest createTutorRequest,
                             String idTutor, Callback<TutorModel> callback){

        tutorRemoteDataSource.updateTutor(createTutorRequest, idTutor, callback);
    }

    public void getTutorName(String uid, Callback<String> callback){
        tutorRemoteDataSource.getTutorName(uid, callback);
    }

    public void getTutorPhotoUri(String uid, Callback<Uri> callback){
        tutorRemoteDataSource.getTutorPhotoUri(uid, callback);
    }

    public void getTutorEmail(String uidTutor, Callback<String> callback){
        tutorRemoteDataSource.getTutorEmail(uidTutor, callback);
    }

    public void getTutorModelById(String tutorId, Callback<TutorModel> callback){
        tutorRemoteDataSource.getTutorModelById(tutorId, callback);
    }



}
