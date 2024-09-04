package com.example.bicoccahelp.data.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.net.Uri;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.tutor.CreateTutorRequest;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRemoteDataSource;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28) // Specifica la versione SDK
public class TutorRepositoryTest {

    @Mock
    private TutorRemoteDataSource mockTutorRemoteDataSource;

    @InjectMocks
    private TutorRepository tutorRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTutor() {
        Map<String, Boolean> disponibilitaGiorni = new HashMap<>();
        disponibilitaGiorni.put("Monday", true);
        ArrayList<String> skills = new ArrayList<>();
        skills.add("Math");
        CreateTutorRequest request = new CreateTutorRequest("Computer Science", disponibilitaGiorni, skills, 4.5);
        Callback<TutorModel> mockCallback = mock(Callback.class);

        tutorRepository.createTutor(request, mockCallback);

        verify(mockTutorRemoteDataSource).createTutor(eq(request), any(Callback.class));
    }

    @Test
    public void testDeleteTutor() {
        String uid = "tutor123";

        tutorRepository.deleteTutor(uid);

        verify(mockTutorRemoteDataSource).deleteTutor(eq(uid));
    }

    @Test
    public void testTutorExist() {
        String uid = "tutor123";
        Callback<Boolean> mockCallback = mock(Callback.class);
        doAnswer(invocation -> {
            Callback<Boolean> callback = invocation.getArgument(1);
            callback.onSucces(true);
            return null;
        }).when(mockTutorRemoteDataSource).tutorExist(eq(uid), any(Callback.class));

        tutorRepository.tutorExist(uid, mockCallback);

        verify(mockTutorRemoteDataSource).tutorExist(eq(uid), any(Callback.class));
        verify(mockCallback).onSucces(true);
    }

    @Test
    public void testUpdateTutorName() {
        String uid = "tutor123";
        String name = "John Doe";

        tutorRepository.updateTutorName(uid, name);

        verify(mockTutorRemoteDataSource).updateTutorName(eq(uid), eq(name));
    }

    @Test
    public void testListTutors() {
        Long limit = 10L;
        Callback<List<TutorModel>> mockCallback = mock(Callback.class);
        List<TutorModel> mockList = new ArrayList<>();
        doAnswer(invocation -> {
            Callback<List<TutorModel>> callback = invocation.getArgument(1);
            callback.onSucces(mockList);
            return null;
        }).when(mockTutorRemoteDataSource).listTutors(eq(limit), any(Callback.class));

        tutorRepository.listTutors(limit, mockCallback);

        verify(mockTutorRemoteDataSource).listTutors(eq(limit), any(Callback.class));
        verify(mockCallback).onSucces(mockList);
    }

    @Test
    public void testListTutorName() {
        String name = "John";
        Long limit = 10L;
        Callback<List<TutorModel>> mockCallback = mock(Callback.class);
        List<TutorModel> mockList = new ArrayList<>();
        doAnswer(invocation -> {
            Callback<List<TutorModel>> callback = invocation.getArgument(2);
            callback.onSucces(mockList);
            return null;
        }).when(mockTutorRemoteDataSource).listTutorName(eq(name), eq(limit), any(Callback.class));

        tutorRepository.listTutorName(name, limit, mockCallback);

        verify(mockTutorRemoteDataSource).listTutorName(eq(name), eq(limit), any(Callback.class));
        verify(mockCallback).onSucces(mockList);
    }

    @Test
    public void testListTutorOrderReview() {
        Long limit = 10L;
        Callback<List<TutorModel>> mockCallback = mock(Callback.class);
        List<TutorModel> mockList = new ArrayList<>();
        doAnswer(invocation -> {
            Callback<List<TutorModel>> callback = invocation.getArgument(1);
            callback.onSucces(mockList);
            return null;
        }).when(mockTutorRemoteDataSource).listTutorOrderReview(eq(limit), any(Callback.class));

        tutorRepository.listTutorOrderReview(limit, mockCallback);

        verify(mockTutorRemoteDataSource).listTutorOrderReview(eq(limit), any(Callback.class));
        verify(mockCallback).onSucces(mockList);
    }

    @Test
    public void testUpdateTutorPhoto() {
        String uid = "tutor123";
        Uri photoUri = Uri.parse("http://example.com/photo.jpg");

        tutorRepository.updateTutorPhoto(uid, photoUri);

        verify(mockTutorRemoteDataSource).updateTutorPhoto(eq(uid), eq(photoUri));
    }


    @Test
    public void testListTutorSkill() {
        String skill = "Math";
        Long limit = 10L;
        Callback<List<TutorModel>> mockCallback = mock(Callback.class);
        List<TutorModel> mockList = new ArrayList<>();
        doAnswer(invocation -> {
            Callback<List<TutorModel>> callback = invocation.getArgument(2);
            callback.onSucces(mockList);
            return null;
        }).when(mockTutorRemoteDataSource).listTutorSkill(eq(skill), eq(limit), any(Callback.class));

        tutorRepository.listTutorSkill(skill, limit, mockCallback);

        verify(mockTutorRemoteDataSource).listTutorSkill(eq(skill), eq(limit), any(Callback.class));
        verify(mockCallback).onSucces(mockList);
    }

    @Test
    public void testListTutorsCorsodiStudi() {
        String idCorso = "course123";
        Long limit = 10L;
        Callback<List<TutorModel>> mockCallback = mock(Callback.class);
        List<TutorModel> mockList = new ArrayList<>();
        doAnswer(invocation -> {
            Callback<List<TutorModel>> callback = invocation.getArgument(2);
            callback.onSucces(mockList);
            return null;
        }).when(mockTutorRemoteDataSource).listTutorsCorsodiStudi(eq(idCorso), eq(limit), any(Callback.class));

        tutorRepository.listTutorsCorsodiStudi(idCorso, limit, mockCallback);

        verify(mockTutorRemoteDataSource).listTutorsCorsodiStudi(eq(idCorso), eq(limit), any(Callback.class));
        verify(mockCallback).onSucces(mockList);
    }

    @Test
    public void testListTutorDisponibility() {
        String day = "Monday";
        Long limit = 10L;
        Callback<List<TutorModel>> mockCallback = mock(Callback.class);
        List<TutorModel> mockList = new ArrayList<>();
        doAnswer(invocation -> {
            Callback<List<TutorModel>> callback = invocation.getArgument(2);
            callback.onSucces(mockList);
            return null;
        }).when(mockTutorRemoteDataSource).listTutorDisponibility(eq(day), eq(limit), any(Callback.class));

        tutorRepository.listTutorDisponibility(day, limit, mockCallback);

        verify(mockTutorRemoteDataSource).listTutorDisponibility(eq(day), eq(limit), any(Callback.class));
        verify(mockCallback).onSucces(mockList);
    }

    @Test
    public void testGetTutorUid() {
        String name = "John Doe";
        Callback<String> mockCallback = mock(Callback.class);
        String mockUid = "tutor123";
        doAnswer(invocation -> {
            Callback<String> callback = invocation.getArgument(1);
            callback.onSucces(mockUid);
            return null;
        }).when(mockTutorRemoteDataSource).getTutorUid(eq(name), any(Callback.class));

        tutorRepository.getTutorUid(name, mockCallback);

        verify(mockTutorRemoteDataSource).getTutorUid(eq(name), any(Callback.class));
        verify(mockCallback).onSucces(mockUid);
    }

    @Test
    public void testUpdateTutor() {
        Map<String, Boolean> disponibilitaGiorni = new HashMap<>();
        disponibilitaGiorni.put("Monday", true);
        ArrayList<String> skills = new ArrayList<>();
        skills.add("Math");
        CreateTutorRequest request = new CreateTutorRequest("Computer Science", disponibilitaGiorni, skills, 4.5);
        String idTutor = "tutor123";
        Callback<TutorModel> mockCallback = mock(Callback.class);

        tutorRepository.updateTutor(request, idTutor, mockCallback);

        verify(mockTutorRemoteDataSource).updateTutor(eq(request), eq(idTutor), any(Callback.class));
    }

    @Test
    public void testGetTutorName() {
        String uid = "tutor123";
        Callback<String> mockCallback = mock(Callback.class);
        String mockName = "John Doe";
        doAnswer(invocation -> {
            Callback<String> callback = invocation.getArgument(1);
            callback.onSucces(mockName);
            return null;
        }).when(mockTutorRemoteDataSource).getTutorName(eq(uid), any(Callback.class));

        tutorRepository.getTutorName(uid, mockCallback);

        verify(mockTutorRemoteDataSource).getTutorName(eq(uid), any(Callback.class));
        verify(mockCallback).onSucces(mockName);
    }

    @Test
    public void testGetTutorPhotoUri() {
        String uid = "tutor123";
        Callback<Uri> mockCallback = mock(Callback.class);
        Uri mockUri = Uri.parse("http://example.com/photo.jpg");
        doAnswer(invocation -> {
            Callback<Uri> callback = invocation.getArgument(1);
            callback.onSucces(mockUri);
            return null;
        }).when(mockTutorRemoteDataSource).getTutorPhotoUri(eq(uid), any(Callback.class));

        tutorRepository.getTutorPhotoUri(uid, mockCallback);

        verify(mockTutorRemoteDataSource).getTutorPhotoUri(eq(uid), any(Callback.class));
        verify(mockCallback).onSucces(mockUri);
    }

    @Test
    public void testGetTutorEmail() {
        String uidTutor = "tutor123";
        Callback<String> mockCallback = mock(Callback.class);
        String mockEmail = "john.doe@example.com";
        doAnswer(invocation -> {
            Callback<String> callback = invocation.getArgument(1);
            callback.onSucces(mockEmail);
            return null;
        }).when(mockTutorRemoteDataSource).getTutorEmail(eq(uidTutor), any(Callback.class));

        tutorRepository.getTutorEmail(uidTutor, mockCallback);

        verify(mockTutorRemoteDataSource).getTutorEmail(eq(uidTutor), any(Callback.class));
        verify(mockCallback).onSucces(mockEmail);
    }

    @Test
    public void testGetTutorModelById() {
        String tutorId = "tutor123";
        Callback<TutorModel> mockCallback = mock(Callback.class);
        TutorModel mockTutorModel = new TutorModel("tutor123", "john.doe@example.com", true, "John Doe", Uri.parse("http://example.com/photo.jpg"), new HashMap<>(), "Computer Science", new ArrayList<>(), 4.5);
        doAnswer(invocation -> {
            Callback<TutorModel> callback = invocation.getArgument(1);
            callback.onSucces(mockTutorModel);
            return null;
        }).when(mockTutorRemoteDataSource).getTutorModelById(eq(tutorId), any(Callback.class));

        tutorRepository.getTutorModelById(tutorId, mockCallback);

        verify(mockTutorRemoteDataSource).getTutorModelById(eq(tutorId), any(Callback.class));
        verify(mockCallback).onSucces(mockTutorModel);
    }
}
