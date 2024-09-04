package com.example.bicoccahelp.data.createRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import com.example.bicoccahelp.data.corsoDiStudi.CreateCorsoDiStudiRequest;

@RunWith(RobolectricTestRunner.class)
public class CreateCorsoDiStudiRequestTest {

    private CreateCorsoDiStudiRequest request;

    @Before
    public void setUp() {
        request = new CreateCorsoDiStudiRequest("Ingegneria Informatica", "Ingegneria", "Laurea Triennale");
    }

    @Test
    public void testConstructor() {
        assertEquals("Ingegneria Informatica", request.getNomeCorso());
        assertEquals("Ingegneria", request.getArea());
        assertEquals("Laurea Triennale", request.getLivello());
    }

    @Test
    public void testSetNomeCorso() {
        request.setNomeCorso("Matematica");
        assertEquals("Matematica", request.getNomeCorso());
    }

    @Test
    public void testSetArea() {
        request.setArea("Scienze");
        assertEquals("Scienze", request.getArea());
    }

    @Test
    public void testSetLivello() {
        request.setLivello("Laurea Magistrale");
        assertEquals("Laurea Magistrale", request.getLivello());
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullNomeCorso() {
        request.setNomeCorso(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullArea() {
        request.setArea(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullLivello() {
        request.setLivello(null);
    }
}
