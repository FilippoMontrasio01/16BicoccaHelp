package com.example.bicoccahelp.data.repository;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.net.Uri;


import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserAssetsRemoteDataSource;
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRemoteDataSource;
import com.example.bicoccahelp.data.user.UserRepository;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(RobolectricTestRunner.class)
public class UserRepositoryTest {

    @Mock
    UserRemoteDataSource userRemoteDataSource;

    @Mock
    UserAssetsRemoteDataSource userAssetsRemoteDataSource;

    @Mock
    Callback<Void> voidCallback;

    @Mock
    Callback<UserModel> userCallback;

    @Mock
    Callback<String> stringCallback;

    private UserRepository userRepository;

    @Before
    public void setup() {
        // Inizializza i mock e la classe da testare
        MockitoAnnotations.initMocks(this);
        userRepository = new UserRepository(userRemoteDataSource, userAssetsRemoteDataSource);
    }

    @Test
    public void testSendEmailVerification() {
        // Testa il metodo sendEmailVerification
        userRepository.sendEmailVerification(voidCallback);

        // Verifica che il metodo del data source sia stato chiamato
        verify(userRemoteDataSource).sendEmailVerification(voidCallback);
    }

    @Test
    public void testReload() {
        // Testa il metodo reload
        userRepository.reload(userCallback);

        // Verifica che il metodo del data source sia stato chiamato
        verify(userRemoteDataSource).reload(userCallback);
    }

    @Test
    public void testUpdateUsername() {
        // Testa l'aggiornamento del nome utente
        String newName = "NewUserName";
        userRepository.updateUsername(newName, voidCallback);

        // Verifica che il data source abbia ricevuto il nome e il callback
        verify(userRemoteDataSource).updateUsername(eq(newName), eq(voidCallback));
    }

    @Test
    public void testUpdatePhoto_Success() {
        // Simula un utente valido
        UserModel mockUser = new UserModel("uid123", "user@example.com", true, "User Name", null);
        when(userRemoteDataSource.getCurrentUser()).thenReturn(mockUser);

        Uri photoUri = Uri.parse("content://some/path");
        String photoPath = "user/" + mockUser.getUid() + "/profile_photo.jpeg";

        // Simula l'upload che restituisce un percorso di successo
        doAnswer(invocation -> {
            Callback<String> callback = invocation.getArgument(2);
            callback.onSucces(photoPath);
            return null;
        }).when(userAssetsRemoteDataSource).upload(eq(photoPath), eq(photoUri), any(Callback.class));

        // Chiama il metodo da testare
        userRepository.updatePhoto(photoUri, voidCallback);

        // Verifica che il percorso della foto sia stato aggiornato correttamente
        verify(userAssetsRemoteDataSource).upload(eq(photoPath), eq(photoUri), any(Callback.class));
        verify(userRemoteDataSource).updatePhoto(eq(Uri.parse(photoPath)), eq(voidCallback));
    }

    @Test
    public void testUpdatePhoto_Failure() {
        // Simula un utente valido
        UserModel mockUser = new UserModel("uid123", "user@example.com", true, "User Name", null);
        when(userRemoteDataSource.getCurrentUser()).thenReturn(mockUser);

        Uri photoUri = Uri.parse("content://some/path");
        String photoPath = "user/" + mockUser.getUid() + "/profile_photo.jpeg";

        // Simula un errore nell'upload
        doAnswer(invocation -> {
            Callback<String> callback = invocation.getArgument(2);
            callback.onFailure(new Exception("Upload failed"));
            return null;
        }).when(userAssetsRemoteDataSource).upload(eq(photoPath), eq(photoUri), any(Callback.class));

        // Chiama il metodo da testare
        userRepository.updatePhoto(photoUri, voidCallback);

        // Verifica che l'upload fallisca e il callback onFailure venga chiamato
        verify(userAssetsRemoteDataSource).upload(eq(photoPath), eq(photoUri), any(Callback.class));
        verify(voidCallback).onFailure(any(Exception.class));
    }

    @Test
    public void testGetCurrentUser() {
        // Simula un utente
        UserModel mockUser = new UserModel("uid123", "user@example.com", true, "User Name", null);
        when(userRemoteDataSource.getCurrentUser()).thenReturn(mockUser);

        // Verifica che getCurrentUser ritorni l'utente simulato
        assertEquals(mockUser, userRepository.getCurrentUser());
        verify(userRemoteDataSource).getCurrentUser();
    }

    @Test
    public void testDeleteUser() {
        // Testa la cancellazione dell'utente
        userRepository.deleteUser(voidCallback);

        // Verifica che il metodo del data source sia stato chiamato
        verify(userRemoteDataSource).deleteUser(voidCallback);
    }
}