package com.example.bicoccahelp.utils;

import com.example.bicoccahelp.data.auth.AuthRemoteDataSource;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRemoteDataSource;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserAssetsRemoteDataSource;
import com.example.bicoccahelp.data.user.UserRemoteDataSource;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRemoteDataSource;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRemoteDataSource;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;

public class ServiceLocator {
    public static volatile ServiceLocator INSTANCE = null;
    private ServiceLocator() {};

    public static ServiceLocator getInstance(){
        if(INSTANCE == null){
            synchronized (ServiceLocator.class){
                if (INSTANCE == null){
                    INSTANCE = new ServiceLocator();
                }
            }
        }

        return INSTANCE;
    }

    public AuthRepository getAuthRepository(){
        return new AuthRepository(new AuthRemoteDataSource());
    }

    public UserRepository getUserRepository(){
        return new UserRepository(new UserRemoteDataSource(), new UserAssetsRemoteDataSource());
    }

    public StudentRepository getStudentRepository(){
        return new StudentRepository(new StudentRemoteDataSource());
    }

    public TutorRepository getTutorRepository(){
        return new TutorRepository((new TutorRemoteDataSource()));
    }

    public CorsoDiStudiRepository getCorsoDiStudiRepository(){
        return new CorsoDiStudiRepository(new CorsoDiStudiRemoteDataSource());
    }
}
