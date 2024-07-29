package com.example.bicoccahelp.data.lesson;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.google.firebase.Timestamp;

import java.util.List;

public class LessonRepository {

    private final LessonRemoteDataSource lessonRemoteDataSource;


    public LessonRepository(LessonRemoteDataSource lessonRemoteDataSource) {
        this.lessonRemoteDataSource = lessonRemoteDataSource;
    }

    public void createLesson(CreateLessonRequest request, Callback<LessonModel> callback){
        lessonRemoteDataSource.createLesson(request, callback);
    }

    public void countLesson(String uidStudent, Timestamp day, Callback<Integer> callback){
        lessonRemoteDataSource.countLesson(uidStudent, day, callback);
    }

    public void checkHourPerDay(String uidStudent, Timestamp day, String hour, Callback<Boolean> callback){
        lessonRemoteDataSource.checkHourPerDay(uidStudent, day, hour, callback);
    }

    public void listLessonsByStudent(String uidStudent, Long limit, Callback<List<LessonModel>> callback){
        lessonRemoteDataSource.listLessonsByStudent(uidStudent,limit, callback);
    }

    public void deleteLesson(String lessonUid, Callback<Void> callback){
        lessonRemoteDataSource.deleteLesson(lessonUid, callback);
    }

}
