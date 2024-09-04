package com.example.bicoccahelp.data.models;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.example.bicoccahelp.data.lesson.LessonModel;
import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class LessonModelTest {

    private LessonModel lessonModel;
    private String id;
    private String uidStudent;
    private String uidTutor;
    private Timestamp data;
    private String ora;
    private String description;

    @Before
    public void setUp() {
        id = "lesson123";
        uidStudent = "student123";
        uidTutor = "tutor123";
        data = Timestamp.now();
        ora = "10:00-11:00";
        description = "Math lesson";
        lessonModel = new LessonModel(id, uidStudent, uidTutor, data, ora, description);
    }

    @Test
    public void testConstructor() {
        assertEquals(id, lessonModel.getId());
        assertEquals(uidStudent, lessonModel.getUid_Student());
        assertEquals(uidTutor, lessonModel.getUid_tutor());
        assertEquals(data, lessonModel.getData());
        assertEquals(ora, lessonModel.getOra());
        assertEquals(description, lessonModel.getDescription());
    }

    @Test
    public void testSetUid_Student() {
        String newUidStudent = "student456";
        lessonModel.setUid_Student(newUidStudent);
        assertEquals(newUidStudent, lessonModel.getUid_Student());
    }

    @Test
    public void testSetUid_tutor() {
        String newUidTutor = "tutor456";
        lessonModel.setUid_tutor(newUidTutor);
        assertEquals(newUidTutor, lessonModel.getUid_tutor());
    }

    @Test
    public void testSetDescription() {
        String newDescription = "Science lesson";
        lessonModel.setDescription(newDescription);
        assertEquals(newDescription, lessonModel.getDescription());
    }

    @Test
    public void testSetData() {
        Timestamp newData = Timestamp.now();
        lessonModel.setData(newData);
        assertEquals(newData, lessonModel.getData());
    }

    @Test
    public void testSetOra() {
        String newOra = "11:00-12:00";
        lessonModel.setOra(newOra);
        assertEquals(newOra, lessonModel.getOra());
    }

    @Test
    public void testSetId() {
        String newId = "lesson456";
        lessonModel.setId(newId);
        assertEquals(newId, lessonModel.getId());
    }

    @Test
    public void testGetUid_Student() {
        assertEquals(uidStudent, lessonModel.getUid_Student());
    }

    @Test
    public void testGetUid_tutor() {
        assertEquals(uidTutor, lessonModel.getUid_tutor());
    }

    @Test
    public void testGetData() {
        assertEquals(data, lessonModel.getData());
    }

    @Test
    public void testGetOra() {
        assertEquals(ora, lessonModel.getOra());
    }

    @Test
    public void testGetDescription() {
        assertEquals(description, lessonModel.getDescription());
    }

    @Test
    public void testGetId() {
        assertEquals(id, lessonModel.getId());
    }
}
