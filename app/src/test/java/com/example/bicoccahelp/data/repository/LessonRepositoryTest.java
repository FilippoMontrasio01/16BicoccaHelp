package com.example.bicoccahelp.data.repository;



import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.lesson.CreateLessonRequest;
import com.example.bicoccahelp.data.lesson.LessonModel;
import com.example.bicoccahelp.data.lesson.LessonRemoteDataSource;
import com.example.bicoccahelp.data.lesson.LessonRepository;
import com.google.firebase.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.firebase.Timestamp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class LessonRepositoryTest {

    @Mock
    private LessonRemoteDataSource mockLessonRemoteDataSource;

    @InjectMocks
    private LessonRepository lessonRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLesson() {
        Callback<LessonModel> mockCallback = mock(Callback.class);
        CreateLessonRequest request = new CreateLessonRequest(
                "uidStudent",
                "uidTutor",
                Timestamp.now(),
                "10:00",
                "Math lesson"
        );
        LessonModel lessonModel = new LessonModel(
                "lessonId",
                "uidStudent",
                "uidTutor",
                Timestamp.now(),
                "10:00",
                "Math lesson"
        );

        // Simula il comportamento del metodo createLesson
        doAnswer(invocation -> {
            Callback<LessonModel> callback = invocation.getArgument(1);
            callback.onSucces(lessonModel);
            return null;
        }).when(mockLessonRemoteDataSource).createLesson(eq(request), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.createLesson(request, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).createLesson(eq(request), any(Callback.class));
    }

    @Test
    public void testCountLesson() {
        Callback<Integer> mockCallback = mock(Callback.class);
        Timestamp timestamp = Timestamp.now();
        Integer count = 5;

        // Simula il comportamento del metodo countLesson
        doAnswer(invocation -> {
            Callback<Integer> callback = invocation.getArgument(2);
            callback.onSucces(count);
            return null;
        }).when(mockLessonRemoteDataSource).countLesson(anyString(), eq(timestamp), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.countLesson("uidStudent", timestamp, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).countLesson(eq("uidStudent"), eq(timestamp), any(Callback.class));
    }

    @Test
    public void testCheckHourPerDay() {
        Callback<Boolean> mockCallback = mock(Callback.class);
        Timestamp timestamp = Timestamp.now();
        String hour = "10:00";
        Boolean result = true;

        // Simula il comportamento del metodo checkHourPerDay
        doAnswer(invocation -> {
            Callback<Boolean> callback = invocation.getArgument(3);
            callback.onSucces(result);
            return null;
        }).when(mockLessonRemoteDataSource).checkHourPerDay(anyString(), eq(timestamp), eq(hour), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.checkHourPerDay("uidStudent", timestamp, hour, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).checkHourPerDay(eq("uidStudent"), eq(timestamp), eq(hour), any(Callback.class));
    }

    @Test
    public void testListLessonsByStudentASC() {
        Callback<List<LessonModel>> mockCallback = mock(Callback.class);
        Long limit = 10L;
        List<LessonModel> mockList = new ArrayList<>();

        // Simula il comportamento del metodo listLessonsByStudentASC
        doAnswer(invocation -> {
            Callback<List<LessonModel>> callback = invocation.getArgument(2);
            callback.onSucces(mockList);
            return null;
        }).when(mockLessonRemoteDataSource).listLessonsByStudentASC(anyString(), eq(limit), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.listLessonsByStudentASC("uidStudent", limit, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).listLessonsByStudentASC(eq("uidStudent"), eq(limit), any(Callback.class));
    }

    @Test
    public void testDeleteLesson() {
        Callback<Void> mockCallback = mock(Callback.class);
        String lessonUid = "lessonUid";

        // Simula il comportamento del metodo deleteLesson
        doAnswer(invocation -> {
            Callback<Void> callback = invocation.getArgument(1);
            callback.onSucces(null);
            return null;
        }).when(mockLessonRemoteDataSource).deleteLesson(eq(lessonUid), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.deleteLesson(lessonUid, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).deleteLesson(eq(lessonUid), any(Callback.class));
    }

    @Test
    public void testListLessonsByStudentDES() {
        Callback<List<LessonModel>> mockCallback = mock(Callback.class);
        Long limit = 10L;
        List<LessonModel> mockList = new ArrayList<>();

        // Simula il comportamento del metodo listLessonsByStudentDES
        doAnswer(invocation -> {
            Callback<List<LessonModel>> callback = invocation.getArgument(2);
            callback.onSucces(mockList);
            return null;
        }).when(mockLessonRemoteDataSource).listLessonsByStudentDES(anyString(), eq(limit), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.listLessonsByStudentDES("uidStudent", limit, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).listLessonsByStudentDES(eq("uidStudent"), eq(limit), any(Callback.class));
    }

    @Test
    public void testListLessonsByTutorDES() {
        Callback<List<LessonModel>> mockCallback = mock(Callback.class);
        Long limit = 10L;
        List<LessonModel> mockList = new ArrayList<>();

        // Simula il comportamento del metodo listLessonsByTutorDES
        doAnswer(invocation -> {
            Callback<List<LessonModel>> callback = invocation.getArgument(3);
            callback.onSucces(mockList);
            return null;
        }).when(mockLessonRemoteDataSource).listLessonsByTutorDES(anyString(), anyString(), eq(limit), any(Callback.class));

        // Chiama il metodo da testare
        lessonRepository.listLessonsByTutorDES("uidStudent", "uidTutor", limit, mockCallback);

        // Verifica che il metodo sia stato chiamato con i parametri corretti
        verify(mockLessonRemoteDataSource).listLessonsByTutorDES(eq("uidStudent"), eq("uidTutor"), eq(limit), any(Callback.class));
    }

    @Test
    public void testListLessonByDate() {
        // Create a mock of the Callback
        Callback<List<LessonModel>> mockCallback = mock(Callback.class);

        // Create sample data
        Timestamp selectedDate = Timestamp.now();
        Long limit = 10L;
        List<LessonModel> mockList = new ArrayList<>();

        // Configure the behavior of mockLessonRemoteDataSource
        doAnswer(invocation -> {
            // Retrieve the Callback passed as an argument
            Callback<List<LessonModel>> callback = invocation.getArgument(3); // Change the index to 3
            // Call the onSuccess method on the callback with the mockList
            callback.onSucces(mockList);
             // Correct the method name to onSuccess
            return null;
        }).when(mockLessonRemoteDataSource).listLessonByDate(
                any(String.class),
                eq(selectedDate),
                eq(limit),
                any(Callback.class)
        );

        // Call the method to be tested
        lessonRepository.listLessonByDate("uidStudent", selectedDate, limit, mockCallback);

        // Verify that the method of the mock was called with the correct parameters
        verify(mockLessonRemoteDataSource).listLessonByDate(
                eq("uidStudent"),
                eq(selectedDate),
                eq(limit),
                any(Callback.class)
        );
    }

}
