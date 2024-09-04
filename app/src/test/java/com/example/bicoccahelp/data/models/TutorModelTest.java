package com.example.bicoccahelp.data.models;

import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

import com.example.bicoccahelp.data.user.tutor.TutorModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class TutorModelTest {

    private TutorModel tutor;
    private Uri photoUri;
    private Map<String, Boolean> disponibilitaGiorni;
    private ArrayList<String> skills;

    @Before
    public void setUp() {
        photoUri = Uri.parse("http://example.com/photo.jpg");
        disponibilitaGiorni = new HashMap<>();
        disponibilitaGiorni.put("Monday", true);
        disponibilitaGiorni.put("Tuesday", false);
        skills = new ArrayList<>();
        skills.add("Java");
        skills.add("Python");
        tutor = new TutorModel("tutor123", "tutor@example.com", true, "John Doe", photoUri, disponibilitaGiorni, "Computer Science", skills, 4.5);
    }

    @Test
    public void testConstructor() {
        assertEquals("tutor123", tutor.getUid());
        assertEquals("tutor@example.com", tutor.getEmail());
        assertTrue(tutor.isEmailVerified());
        assertEquals("John Doe", tutor.getName());
        assertEquals(photoUri, tutor.getPhotoUri());
        assertEquals(disponibilitaGiorni, tutor.getDisponibilitaGiorni());
        assertEquals("Computer Science", tutor.getCorsoDiStudi());
        assertEquals(skills, tutor.getSkills());
        assertEquals(4.5, tutor.getAverageReview(), 0.0);
    }

    @Test
    public void testSetDisponibilities() {
        tutor.setDisponibilities("Wednesday", true);
        assertTrue(Boolean.TRUE.equals(tutor.getDisponibilitaGiorni().get("Wednesday")));
    }

    @Test
    public void testSetCorsoDiStudi() {
        tutor.setCorsoDiStudi("Mathematics");
        assertEquals("Mathematics", tutor.getCorsoDiStudi());
    }

    @Test
    public void testSetSkills() {
        ArrayList<String> newSkills = new ArrayList<>();
        newSkills.add("C++");
        newSkills.add("JavaScript");
        tutor.setSkills(newSkills);
        assertEquals(newSkills, tutor.getSkills());
    }

    @Test
    public void testSetAverageReview() {
        tutor.setAverageReview(4.8);
        assertEquals(4.8, tutor.getAverageReview(), 0.0);
    }
}
