package com.example.bicoccahelp.data.createRequest;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.example.bicoccahelp.data.review.CreateReviewRequest;

public class CreateReviewRequestTest {

    private CreateReviewRequest request;

    @Before
    public void setUp() {
        request = new CreateReviewRequest("tutor123", "student456", 4.5);
    }

    @Test
    public void testConstructor() {
        assertEquals("tutor123", request.getUidTutor());
        assertEquals("student456", request.getUidStudent());
        assertEquals(4.5, request.getStars(), 0.0);
    }

    @Test
    public void testSetStars() {
        request.setStars(5.0);
        assertEquals(5.0, request.getStars(), 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUidTutor() {
        new CreateReviewRequest(null, "student456", 4.5);
    }

    @Test(expected = NullPointerException.class)
    public void testNonNullUidStudent() {
        new CreateReviewRequest("tutor123", null, 4.5);
    }

    @Test
    public void testStarsRange() {
        request.setStars(0.0);
        assertEquals(0.0, request.getStars(), 0.0);

        request.setStars(5.0);
        assertEquals(5.0, request.getStars(), 0.0);

        try {
            request.setStars(-1.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            request.setStars(6.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetUidTutor() {
        assertEquals("tutor123", request.getUidTutor());
    }

    @Test
    public void testGetUidStudent() {
        assertEquals("student456", request.getUidStudent());
    }

    @Test
    public void testGetStars() {
        assertEquals(4.5, request.getStars(), 0.0);
    }
}
