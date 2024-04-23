package com.example.bicoccahelp.data.user.tutor;

import com.example.bicoccahelp.data.Callback;

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

    public void updateTutor(CreateTutorRequest createTutorRequest,
                            String idTutor, Callback<TutorModel> callback){

        tutorRemoteDataSource.updateTutor(createTutorRequest, idTutor, callback);
    }

    public void updateTutorName(String uid, String name){
        tutorRemoteDataSource.updateTutorName(uid, name);
    }

    public void getTutorId(String uid, Callback<String> callback){
        tutorRemoteDataSource.getTutorId(uid, callback);
    }
}
