package com.example.bicoccahelp.data.user.student;

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

    public void updateStudentName(String uri, String name){
        studentRemoteDataSource.updateStudentName(uri, name);
    }

    public void deleteStudent(String uid){
        studentRemoteDataSource.deleteStudent(uid);
    }

    public void isTutor(String uid, boolean isTutor, Callback<Boolean> callback){
        studentRemoteDataSource.isTutor(uid, isTutor, callback);
    }

}
