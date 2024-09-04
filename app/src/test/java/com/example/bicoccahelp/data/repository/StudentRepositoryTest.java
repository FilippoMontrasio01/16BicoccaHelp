package com.example.bicoccahelp.data.repository;

import android.net.Uri;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRemoteDataSource;
import com.example.bicoccahelp.data.user.student.StudentRepository;

// Esegui i test con Robolectric
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)  // Specifica la versione SDK di Android
public class StudentRepositoryTest {

    @Mock
    private StudentRemoteDataSource studentRemoteDataSource;

    @Mock
    private Callback<StudentModel> studentCallback;

    @Mock
    private Callback<Boolean> booleanCallback;

    @Mock
    private Callback<String> stringCallback;

    private StudentRepository studentRepository;

    @Before
    public void setUp() {
        // Inizializza i mock
        MockitoAnnotations.initMocks(this);
        studentRepository = new StudentRepository(studentRemoteDataSource);
    }

    @Test
    public void testCreateStudent_Success() {
        CreateStudentRequest createStudentRequest = new CreateStudentRequest("Informatica", true);

        // Simula la chiamata createStudent nel repository
        studentRepository.createStudent(createStudentRequest, studentCallback);

        // Verifica che il metodo createStudent del data source venga chiamato con i parametri corretti
        verify(studentRemoteDataSource).createStudent(eq(createStudentRequest), eq(studentCallback));
    }

    @Test
    public void testStudentExist_Success() {
        String uid = "uid123";

        // Chiamata al metodo studentExist
        studentRepository.studentExist(uid, booleanCallback);

        // Verifica che il metodo studentExist venga chiamato con i parametri corretti
        verify(studentRemoteDataSource).studentExist(eq(uid), eq(booleanCallback));
    }

    @Test
    public void testUpdateStudentName_Success() {
        String uid = "uid123";
        String newName = "Updated Name";

        // Chiamata al metodo updateStudentName
        studentRepository.updateStudentName(uid, newName);

        // Verifica che il metodo updateStudentName venga chiamato correttamente
        verify(studentRemoteDataSource).updateStudentName(eq(uid), eq(newName));
    }

    @Test
    public void testUpdateIsTutor_Success() {
        String uid = "uid123";
        boolean isTutor = true;

        // Chiamata al metodo updateiSTutor
        studentRepository.updateiSTutor(uid, isTutor);

        // Verifica che updateiSTutor venga chiamato correttamente
        verify(studentRemoteDataSource).updateIsTutor(eq(uid), eq(isTutor));
    }

    @Test
    public void testDeleteStudent_Success() {
        String uid = "uid123";

        // Chiamata al metodo deleteStudent
        studentRepository.deleteStudent(uid);

        // Verifica che il metodo deleteStudent del data source venga chiamato correttamente
        verify(studentRemoteDataSource).deleteStudent(eq(uid));
    }

    @Test
    public void testIsTutor_Success() {
        String uid = "uid123";
        boolean isTutor = true;

        // Chiamata al metodo isTutor
        studentRepository.isTutor(uid, isTutor, booleanCallback);

        // Verifica che il metodo isTutor venga chiamato correttamente
        verify(studentRemoteDataSource).isTutor(eq(uid), eq(isTutor), eq(booleanCallback));
    }

    @Test
    public void testGetCorsoDiStudi_Success() {
        String uid = "uid123";

        // Chiamata al metodo getCorsoDiStudi
        studentRepository.getCorsoDiStudi(uid, stringCallback);

        // Verifica che il metodo getCorsoDiStudi venga chiamato correttamente
        verify(studentRemoteDataSource).getCorsoDiStudi(eq(uid), eq(stringCallback));
    }

    @Test
    public void testUpdateStudentPhoto_Success() {
        String uid = "uid123";
        Uri mockUri = mock(Uri.class);  // Mock dell'Uri

        // Chiamata al metodo updateStudentPhoto
        studentRepository.updateStudentPhoto(uid, mockUri);

        // Verifica che il metodo updateStudentPhoto venga chiamato correttamente
        verify(studentRemoteDataSource).updateStudentPhoto(eq(uid), eq(mockUri));
    }

    @Test
    public void testUpdateStudyProgram_Success() {
        String uid = "uid123";
        String studyProgram = "Ingegneria";

        // Chiamata al metodo updateStudyProgram
        studentRepository.updateStudyProgram(uid, studyProgram);

        // Verifica che il metodo updateStudyProgram venga chiamato correttamente
        verify(studentRemoteDataSource).updateStudyProgram(eq(uid), eq(studyProgram));
    }
}
