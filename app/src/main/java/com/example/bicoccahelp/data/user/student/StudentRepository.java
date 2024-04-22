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
}
