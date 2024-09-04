package com.example.bicoccahelp.data.models;

import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import com.example.bicoccahelp.data.user.UserModel;

@RunWith(RobolectricTestRunner.class)
public class UserModelTest {

    private UserModel user;
    private Uri photoUri;

    @Before
    public void setUp() {
        photoUri = Uri.parse("http://example.com/photo.jpg");
        user = new UserModel("user123", "user@example.com", true, "John Doe", photoUri);
    }

    @Test
    public void testConstructor() {
        assertEquals("user123", user.getUid());
        assertEquals("user@example.com", user.getEmail());
        assertTrue(user.isEmailVerified());
        assertEquals("John Doe", user.getName());
        assertEquals(photoUri, user.getPhotoUri());
    }

    @Test
    public void testSetName() {
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    public void testSetPhotoUri() {
        Uri newPhotoUri = Uri.parse("http://example.com/newphoto.jpg");
        user.setPhotoUri(newPhotoUri);
        assertEquals(newPhotoUri, user.getPhotoUri());
    }

    @Test
    public void testSetEmailVerified() {
        user.setEmailVerified(false);
        assertFalse(user.isEmailVerified());
    }
}
