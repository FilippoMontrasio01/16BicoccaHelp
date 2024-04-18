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
}
