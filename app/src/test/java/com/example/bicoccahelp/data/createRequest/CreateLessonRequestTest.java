package com.example.bicoccahelp.data.createRequest;

import com.example.bicoccahelp.data.lesson.CreateLessonRequest;
import com.google.firebase.Timestamp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Objects;

public class CreateLessonRequestTest {

    private CreateLessonRequest request;
    private Timestamp data;

    @Before
    public void setUp() {
        data = Timestamp.now(); // Utilizza l'ora corrente per il test
        request = new CreateLessonRequest("student123", "tutor123", data, "10:00", "Lezione di matematica");
    }

    @Test
    public void testConstructor() {
        assertEquals("student123", request.getUid_Student());
        assertEquals("tutor123", request.getUid_tutor());
        assertEquals(data, request.getData());
        assertEquals("10:00", request.getOra());
        assertEquals("Lezione di matematica", request.getDescription());
    }

    @Test
    public void testSetUid_Student() {
        request.setUid_Student("newStudent123");
        assertEquals("newStudent123", request.getUid_Student());
    }

    @Test
    public void testSetUid_tutor() {
        request.setUid_tutor("newTutor123");
        assertEquals("newTutor123", request.getUid_tutor());
    }

    @Test
    public void testSetData() {
        Timestamp newData = Timestamp.now(); // Utilizza l'ora corrente per il test
        request.setData(newData);
        assertEquals(newData, request.getData());
    }

    @Test
    public void testSetOra() {
        request.setOra("11:00");
        assertEquals("11:00", request.getOra());
    }

    @Test
    public void testSetDescription() {
        request.setDescription("Lezione di fisica");
        assertEquals("Lezione di fisica", request.getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUid_Student() {
        request.setUid_Student(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUid_tutor() {
        request.setUid_tutor(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullData() {
        request.setData(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullOra() {
        request.setOra(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullDescription() {
        request.setDescription(null);
    }
}
