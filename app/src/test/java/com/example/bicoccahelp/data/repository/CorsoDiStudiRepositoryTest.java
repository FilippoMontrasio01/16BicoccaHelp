package com.example.bicoccahelp.data.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiModel;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRemoteDataSource;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.corsoDiStudi.CreateCorsoDiStudiRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CorsoDiStudiRepositoryTest {

    @Mock
    private CorsoDiStudiRemoteDataSource mockRemoteDataSource;

    @InjectMocks
    private CorsoDiStudiRepository corsoDiStudiRepository;

    @Before
    public void setUp() {
        // Inizializza i mock
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCorso() {
        Callback<CorsoDiStudiModel> mockCallback = mock(Callback.class);
        CreateCorsoDiStudiRequest request = new CreateCorsoDiStudiRequest("corso", "area", "livello");

        corsoDiStudiRepository.createCorso(request, mockCallback);

        verify(mockRemoteDataSource).createCorso(eq(request), any(Callback.class));
    }

    @Test
    public void testCorsoDiStudiExists() {
        Callback<Boolean> mockCallback = mock(Callback.class);

        corsoDiStudiRepository.corsoDiStudiExists("Corso1", mockCallback);

        verify(mockRemoteDataSource).corsoDiStudiExists(eq("Corso1"), any(Callback.class));
    }

    @Test
    public void testGetCorsoDiStudiIdByName() {
        Callback<String> mockCallback = mock(Callback.class);

        corsoDiStudiRepository.getCorsoDiStudiIdByName("Corso1", "Livello1", mockCallback);

        verify(mockRemoteDataSource).getCorsoDiStudiIdByName(eq("Corso1"), eq("Livello1"), any(Callback.class));
    }

    @Test
    public void testGetCorsoId() {
        Callback<String> mockCallback = mock(Callback.class);

        corsoDiStudiRepository.getCorsoId("Corso1", mockCallback);

        verify(mockRemoteDataSource).getCorsoId(eq("Corso1"), any(Callback.class));
    }

    @Test
    public void testGetCorsodiStudiName() {
        Callback<String> mockCallback = mock(Callback.class);

        corsoDiStudiRepository.getCorsodiStudiName("ID1", mockCallback);

        verify(mockRemoteDataSource).getCorsodiStudiName(eq("ID1"), any(Callback.class));
    }

    @Test
    public void testGetCorsoDiStudiLivello() {
        Callback<String> mockCallback = mock(Callback.class);

        corsoDiStudiRepository.getCorsoDiStudiLivello("ID1", mockCallback);

        verify(mockRemoteDataSource).getCorsoDiStudiLivello(eq("ID1"), any(Callback.class));
    }

    @Test
    public void testGetArea() {
        Callback<String> mockCallback = mock(Callback.class);

        corsoDiStudiRepository.getArea("ID1", mockCallback);

        verify(mockRemoteDataSource).getArea(eq("ID1"), any(Callback.class));
    }
}