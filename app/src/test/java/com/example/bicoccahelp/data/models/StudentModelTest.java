package com.example.bicoccahelp.data.models;

import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

import com.example.bicoccahelp.data.user.student.StudentModel;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class StudentModelTest {

    private StudentModel student;
    private Uri photoUri;

    @Before
    public void setUp() {
        photoUri = Uri.parse("http://example.com/photo.jpg");
        student = new StudentModel("student123", "student@example.com", true, "John Doe", photoUri, "Computer Science", true);
    }

    @Test
    public void testConstructor() {
        assertEquals("student123", student.getUid());
        assertEquals("student@example.com", student.getEmail());
        assertTrue(student.isEmailVerified());
        assertEquals("John Doe", student.getName());
        assertEquals(photoUri, student.getPhotoUri());
        assertEquals("Computer Science", student.getCorsoDiStudi());
        assertTrue(student.isTutor());
    }

    @Test
    public void testSetName() {
        student.setName("Jane Doe");
        assertEquals("Jane Doe", student.getName());
    }

    @Test
    public void testSetPhotoUri() {
        Uri newPhotoUri = Uri.parse("http://example.com/newphoto.jpg");
        student.setPhotoUri(newPhotoUri);
        assertEquals(newPhotoUri, student.getPhotoUri());
    }

    @Test
    public void testSetEmailVerified() {
        student.setEmailVerified(false);
        assertFalse(student.isEmailVerified());
    }

    @Test
    public void testSetCorsoDiStudi() {
        student.setCorsoDiStudi("Mathematics");
        assertEquals("Mathematics", student.getCorsoDiStudi());
    }

    @Test
    public void testSetTutor() {
        student.setTutor(false);
        assertFalse(student.isTutor());
    }
}

