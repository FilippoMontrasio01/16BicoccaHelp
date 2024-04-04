package com.example.bicoccahelp.utils;

import com.example.bicoccahelp.data.auth.AuthRemoteDataSource;
import com.example.bicoccahelp.data.auth.AuthRepository;
import com.example.bicoccahelp.data.user.UserRemoteDataSource;
import com.example.bicoccahelp.data.user.UserRepository;

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
        return new UserRepository(new UserRemoteDataSource());
    }
}
