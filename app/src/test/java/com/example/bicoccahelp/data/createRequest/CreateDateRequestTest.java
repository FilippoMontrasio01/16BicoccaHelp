package com.example.bicoccahelp.data.createRequest;

import com.example.bicoccahelp.data.date.CreateDateRequest;
import com.google.firebase.Timestamp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

public class CreateDateRequestTest {

    private CreateDateRequest request;
    private Map<String, Boolean> disponibilitaOrari;
    private Timestamp data;

    @Before
    public void setUp() {
        disponibilitaOrari = new HashMap<>();
        disponibilitaOrari.put("09:00", true);
        disponibilitaOrari.put("10:00", false);
        data = Timestamp.now(); // Utilizza l'ora corrente per il test
        request = new CreateDateRequest(disponibilitaOrari, data, "tutor123");
    }

    @Test
    public void testConstructor() {
        assertEquals(disponibilitaOrari, request.getDisponibilitaOrari());
        assertEquals(data, request.getData());
        assertEquals("tutor123", request.getUidTutor());
    }

    @Test
    public void testSetDisponibilitaOrari() {
        Map<String, Boolean> newDisponibilitaOrari = new HashMap<>();
        newDisponibilitaOrari.put("11:00", true);
        request.setDisponibilitaOrari(newDisponibilitaOrari);
        assertEquals(newDisponibilitaOrari, request.getDisponibilitaOrari());
    }

    @Test
    public void testSetData() {
        Timestamp newData = Timestamp.now(); // Utilizza l'ora corrente per il test
        request.setData(newData);
        assertEquals(newData, request.getData());
    }

    @Test
    public void testSetUidTutor() {
        request.setUidTutor("newTutor123");
        assertEquals("newTutor123", request.getUidTutor());
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullDisponibilitaOrari() {
        request.setDisponibilitaOrari(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullData() {
        request.setData(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUidTutor() {
        request.setUidTutor(null);
    }
}

