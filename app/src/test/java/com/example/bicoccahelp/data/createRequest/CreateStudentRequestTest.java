package com.example.bicoccahelp.data.createRequest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.bicoccahelp.data.user.student.CreateStudentRequest;

public class CreateStudentRequestTest {

    private CreateStudentRequest request;

    @Before
    public void setUp() {
        request = new CreateStudentRequest("Ingegneria Informatica", true);
    }

    @Test
    public void testConstructor() {
        assertEquals("Ingegneria Informatica", request.getCorsoDiStudi());
        assertTrue(request.isTutor());
    }

    @Test
    public void testSetCorsoDiStudi() {
        request.setCorsoDiStudi("Matematica");
        assertEquals("Matematica", request.getCorsoDiStudi());
    }

    @Test
    public void testSetTutor() {
        request.setTutor(false);
        assertFalse(request.isTutor());
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullCorsoDiStudi() {
        request.setCorsoDiStudi(null);
    }

    @Test
    public void testGetCorsoDiStudi() {
        assertEquals("Ingegneria Informatica", request.getCorsoDiStudi());
    }

    @Test
    public void testIsTutor() {
        assertTrue(request.isTutor());
    }
}
