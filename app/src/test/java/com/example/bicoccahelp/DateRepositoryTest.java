package com.example.bicoccahelp;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.date.CreateDateRequest;
import com.example.bicoccahelp.data.date.DateModel;
import com.example.bicoccahelp.data.date.DateRemoteDataSource;
import com.example.bicoccahelp.data.date.DateRepository;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateRepositoryTest {

    @Mock
    private DateRemoteDataSource mockDateRemoteDataSource;

    @InjectMocks
    private DateRepository dateRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateDate() {
        // Crea una mappa di disponibilità per il test
        Map<String, Boolean> disponibilitaOrari = new HashMap<>();
        disponibilitaOrari.put("09:00", true);
        disponibilitaOrari.put("10:00", false);

        // Crea un'istanza di Timestamp per il test
        Timestamp timestamp = Timestamp.now();

        // Crea un'istanza di CreateDateRequest con i parametri richiesti
        CreateDateRequest request = new CreateDateRequest(disponibilitaOrari, timestamp, "uidTutor");

        // Crea un mock del callback
        Callback<DateModel> mockCallback = mock(Callback.class);

        // Esegui il test
        dateRepository.createDate(request, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockDateRemoteDataSource).createDate(eq(request), any(Callback.class));
    }

    @Test
    public void testListOrari() {
        Callback<List<String>> mockCallback = mock(Callback.class);
        Timestamp timestamp = Timestamp.now();
        Long limit = 10L;
        List<String> mockList = new ArrayList<>();

        // Simula la risposta del metodo listOrari
        doAnswer(invocation -> {
            Callback<List<String>> callback = invocation.getArgument(3);
            callback.onSucces(mockList);  // Invoca il metodo onSucces con mockList
            return null;  // Il metodo è void, quindi ritorna null
        }).when(mockDateRemoteDataSource).listOrari(any(String.class), any(Timestamp.class), any(Long.class), any(Callback.class));

        // Chiama il metodo da testare
        dateRepository.listOrari("uidTutor", timestamp, limit, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockDateRemoteDataSource).listOrari(eq("uidTutor"), eq(timestamp), eq(limit), any(Callback.class));
    }

    @Test
    public void testUpdateDate() {
        Callback<Void> mockCallback = mock(Callback.class);
        Timestamp oldDate = Timestamp.now();
        Timestamp newDate = Timestamp.now();

        dateRepository.updateDate("uidTutor", oldDate, newDate, mockCallback);

        verify(mockDateRemoteDataSource).updateDate(eq("uidTutor"), eq(oldDate), eq(newDate), any(Callback.class));
    }

    @Test
    public void testUpdateOrario() {
        Callback<Void> mockCallback = mock(Callback.class);
        Timestamp date = Timestamp.now();
        String orario = "10:00";

        dateRepository.updateOrario("uidTutor", date, orario, mockCallback);

        verify(mockDateRemoteDataSource).updateOrario(eq("uidTutor"), eq(date), eq(orario), any(Callback.class));
    }

    @Test
    public void testResetOrario() {
        Callback<Void> mockCallback = mock(Callback.class);
        Timestamp date = Timestamp.now();
        String orario = "10:00";

        dateRepository.resetOrario("uidTutor", date, orario, mockCallback);

        verify(mockDateRemoteDataSource).resetOrario(eq("uidTutor"), eq(date), eq(orario), any(Callback.class));
    }
}
