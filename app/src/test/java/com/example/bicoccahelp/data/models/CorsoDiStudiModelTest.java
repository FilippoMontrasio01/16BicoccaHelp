package com.example.bicoccahelp.data.models;

import static org.junit.Assert.*;

import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiModel;

import org.junit.Before;
import org.junit.Test;

public class CorsoDiStudiModelTest {

    private CorsoDiStudiModel corsoDiStudiModel;

    @Before
    public void setUp() {
        corsoDiStudiModel = new CorsoDiStudiModel("1", "Informatica", "Tecnologia", "Laurea");
    }

    @Test
    public void testConstructor() {
        assertEquals("1", corsoDiStudiModel.getIdCorso());
        assertEquals("Informatica", corsoDiStudiModel.getNomeCorso());
        assertEquals("Tecnologia", corsoDiStudiModel.getArea());
        assertEquals("Laurea", corsoDiStudiModel.getLivello());
    }

    @Test
    public void testSetIdCorso() {
        corsoDiStudiModel.setIdCorso("2");
        assertEquals("2", corsoDiStudiModel.getIdCorso());
    }

    @Test
    public void testSetNomeCorso() {
        corsoDiStudiModel.setNomeCorso("Matematica");
        assertEquals("Matematica", corsoDiStudiModel.getNomeCorso());
    }

    @Test
    public void testSetArea() {
        corsoDiStudiModel.setArea("Scienze");
        assertEquals("Scienze", corsoDiStudiModel.getArea());
    }

    @Test
    public void testSetLivello() {
        corsoDiStudiModel.setLivello("Master");
        assertEquals("Master", corsoDiStudiModel.getLivello());
    }

    @Test
    public void testGetIdCorso() {
        assertEquals("1", corsoDiStudiModel.getIdCorso());
    }

    @Test
    public void testGetNomeCorso() {
        assertEquals("Informatica", corsoDiStudiModel.getNomeCorso());
    }

    @Test
    public void testGetArea() {
        assertEquals("Tecnologia", corsoDiStudiModel.getArea());
    }

    @Test
    public void testGetLivello() {
        assertEquals("Laurea", corsoDiStudiModel.getLivello());
    }
}
