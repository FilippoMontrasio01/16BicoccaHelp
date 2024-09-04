package com.example.bicoccahelp.data.models;

import static org.junit.Assert.*;

import com.example.bicoccahelp.data.date.DateModel;
import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;



public class DateModelTest {

    private DateModel dateModel;
    private Map<String, Boolean> disponibilitaOrari;
    private Timestamp data;
    private String uidTutor;

    @Before
    public void setUp() {
        disponibilitaOrari = new HashMap<>();
        disponibilitaOrari.put("09:00-10:00", true);
        disponibilitaOrari.put("10:00-11:00", false);
        data = Timestamp.now();
        uidTutor = "tutor123";
        dateModel = new DateModel(disponibilitaOrari, data, uidTutor);
    }

    @Test
    public void testConstructor() {
        assertEquals(disponibilitaOrari, dateModel.getDisponibilitaOrari());
        assertEquals(data, dateModel.getData());
        assertEquals(uidTutor, dateModel.getUidTutor());
    }

    @Test
    public void testSetDisponibilitaOrari() {
        Map<String, Boolean> newDisponibilitaOrari = new HashMap<>();
        newDisponibilitaOrari.put("11:00-12:00", true);
        dateModel.setDisponibilitaOrari(newDisponibilitaOrari);
        assertEquals(newDisponibilitaOrari, dateModel.getDisponibilitaOrari());
    }

    @Test
    public void testSetData() {
        Timestamp newData = Timestamp.now();
        dateModel.setData(newData);
        assertEquals(newData, dateModel.getData());
    }

    @Test
    public void testSetUidTutor() {
        String newUidTutor = "tutor456";
        dateModel.setUidTutor(newUidTutor);
        assertEquals(newUidTutor, dateModel.getUidTutor());
    }

    @Test
    public void testGetDisponibilitaOrari() {
        assertEquals(disponibilitaOrari, dateModel.getDisponibilitaOrari());
    }

    @Test
    public void testGetData() {
        assertEquals(data, dateModel.getData());
    }

    @Test
    public void testGetUidTutor() {
        assertEquals(uidTutor, dateModel.getUidTutor());
    }
}
