package com.example.bicoccahelp.data.lesson;

import com.example.bicoccahelp.data.Callback;

public class LessonRepository {

    private final LessonRemoteDataSource lessonRemoteDataSource;


    public LessonRepository(LessonRemoteDataSource lessonRemoteDataSource) {
        this.lessonRemoteDataSource = lessonRemoteDataSource;
    }

    public void createLesson(CreateLessonRequest request, Callback<LessonModel> callback){
        lessonRemoteDataSource.createLesson(request, callback);
    }
}
