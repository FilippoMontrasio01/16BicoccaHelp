package com.example.bicoccahelp.data.user.student;

import android.net.Uri;

import com.example.bicoccahelp.data.Callback;

import java.util.concurrent.Callable;

public class StudentRepository {
    private final StudentRemoteDataSource studentRemoteDataSource;

    public StudentRepository(StudentRemoteDataSource studentRemoteDataSource) {
        this.studentRemoteDataSource = studentRemoteDataSource;
    }

    public void createStudent(CreateStudentRequest createStudentRequest,
                              Callback<StudentModel> callback){
        studentRemoteDataSource.createStudent(createStudentRequest, callback);
    }

    public void studentExist(String uid, Callback<Boolean> callback){
        studentRemoteDataSource.studentExist(uid, callback);
    }

    public void updateStudentName(String uid, String name){
        studentRemoteDataSource.updateStudentName(uid, name);
    }

    public void updateiSTutor(String uid, boolean isTutor){
        studentRemoteDataSource.updateIsTutor(uid, isTutor);
    }

    public void deleteStudent(String uid){
        studentRemoteDataSource.deleteStudent(uid);
    }

    public void isTutor(String uid, boolean isTutor, Callback<Boolean> callback){
        studentRemoteDataSource.isTutor(uid, isTutor, callback);
    }

    public void getCorsoDiStudi(String uid, Callback<String > callback){
        studentRemoteDataSource.getCorsoDiStudi(uid, callback);
    }

    public void updateStudentPhoto(String uid, Uri photoUri){
        studentRemoteDataSource.updateStudentPhoto(uid, photoUri);
    }

}
