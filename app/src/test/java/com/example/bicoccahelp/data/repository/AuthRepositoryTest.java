package com.example.bicoccahelp.data.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.auth.AuthRemoteDataSource;
import com.example.bicoccahelp.data.auth.AuthRepository;

import static org.mockito.Mockito.*;

public class AuthRepositoryTest {

    @Mock
    private AuthRemoteDataSource authRemoteDataSource;

    @InjectMocks
    private AuthRepository authRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        Callback<Void> callback = mock(Callback.class);
        authRepository.register("test@example.com", "password123", callback);

        verify(authRemoteDataSource).register("test@example.com", "password123", callback);
    }

    @Test
    public void testLogin() {
        Callback<Void> callback = mock(Callback.class);
        authRepository.login("test@example.com", "password123", callback);

        verify(authRemoteDataSource).login("test@example.com", "password123", callback);
    }

    @Test
    public void testForgotPassword() {
        Callback<Void> callback = mock(Callback.class);
        authRepository.forgotPassword("test@example.com", callback);

        verify(authRemoteDataSource).forgotPassword("test@example.com", callback);
    }

    @Test
    public void testLogout() {
        authRepository.logout();
        verify(authRemoteDataSource).logout();
    }
}

