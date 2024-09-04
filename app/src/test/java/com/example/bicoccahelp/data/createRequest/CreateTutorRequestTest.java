package com.example.bicoccahelp.data.createRequest;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.bicoccahelp.data.user.tutor.CreateTutorRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateTutorRequestTest {

    private CreateTutorRequest request;
    private Map<String, Boolean> disponibilitaGiorni;
    private ArrayList<String> skills;

    @Before
    public void setUp() {
        disponibilitaGiorni = new HashMap<>();
        disponibilitaGiorni.put("Monday", true);
        disponibilitaGiorni.put("Tuesday", false);
        skills = new ArrayList<>();
        skills.add("Java");
        skills.add("Python");
        request = new CreateTutorRequest("Ingegneria Informatica", disponibilitaGiorni, skills, 4.5);
    }

    @Test
    public void testConstructor() {
        assertEquals("Ingegneria Informatica", request.getCorsoDiStudi());
        assertEquals(disponibilitaGiorni, request.getDisponibilitaGiorni());
        assertEquals(skills, request.getSkills());
        assertEquals(4.5, request.getAverageReview(), 0.0);
    }

    @Test
    public void testSetCorsoDiStudi() {
        request.setCorsoDiStudi("Matematica");
        assertEquals("Matematica", request.getCorsoDiStudi());
    }

    @Test
    public void testSetDisponibilitaGiorni() {
        Map<String, Boolean> newDisponibilitaGiorni = new HashMap<>();
        newDisponibilitaGiorni.put("Wednesday", true);
        request.setDisponibilitaGiorni(newDisponibilitaGiorni);
        assertEquals(newDisponibilitaGiorni, request.getDisponibilitaGiorni());
    }

    @Test
    public void testSetSkills() {
        ArrayList<String> newSkills = new ArrayList<>();
        newSkills.add("C++");
        newSkills.add("JavaScript");
        request.setSkills(newSkills);
        assertEquals(newSkills, request.getSkills());
    }

    @Test
    public void testSetAverageReview() {
        request.setAverageReview(4.8);
        assertEquals(4.8, request.getAverageReview(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullCorsoDiStudi() {
        request.setCorsoDiStudi(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullDisponibilitaGiorni() {
        request.setDisponibilitaGiorni(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullSkills() {
        request.setSkills(null);
    }
}