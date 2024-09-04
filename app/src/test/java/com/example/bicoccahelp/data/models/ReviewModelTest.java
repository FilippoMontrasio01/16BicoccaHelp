package com.example.bicoccahelp.data.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.bicoccahelp.data.review.ReviewModel;

public class ReviewModelTest {

    private ReviewModel review;

    @Before
    public void setUp() {
        review = new ReviewModel("tutor123", "student456", 4.5);
    }

    @Test
    public void testConstructor() {
        assertEquals("tutor123", review.getUidTutor());
        assertEquals("student456", review.getUidStudent());
        assertEquals(4.5, review.getStars(), 0.0);
    }

    @Test
    public void testSetUidTutor() {
        review.setUidTutor("newTutor");
        assertEquals("newTutor", review.getUidTutor());
    }

    @Test
    public void testSetUidStudent() {
        review.setUidStudent("newStudent");
        assertEquals("newStudent", review.getUidStudent());
    }

    @Test
    public void testSetStars() {
        review.setStars(5.0);
        assertEquals(5.0, review.getStars(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUidTutor() {
        review.setUidTutor(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUidStudent() {
        review.setUidStudent(null);
    }

    @Test
    public void testStarsRange() {
        review.setStars(0.0);
        assertEquals(0.0, review.getStars(), 0.0);

        review.setStars(5.0);
        assertEquals(5.0, review.getStars(), 0.0);

        try {
            review.setStars(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            review.setStars(6.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
