package com.example.bicoccahelp.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;

public class CompleteStudentProfileViewModel extends ViewModel {

    private StudentRepository studentRepository;
    private CorsoDiStudiRepository corsoDiStudiRepository;
    private UserRepository userRepository;
    private TutorRepository tutorRepository;

    private MutableLiveData<Boolean> createStudentSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void initRepositories() {
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
    }

    public LiveData<Boolean> getCreateStudentSuccess() {
        return createStudentSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void getStudyProgramLevel(String studyProgram, boolean isMagistrale, boolean isTutor) {
        String livello = isMagistrale ? "Magistrale" : "Triennale";

        InputValidator.isValidStudyProgram(studyProgram, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                if (exist) {
                    getCorsoId(studyProgram, livello, isTutor);
                } else {
                    errorMessage.setValue("Invalid study program");
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue(String.valueOf(R.string.generic_error));
            }
        });
    }

    private void getCorsoId(String studyProgram, String livello, Boolean isTutor) {
        corsoDiStudiRepository.getCorsoDiStudiIdByName(studyProgram, livello, new Callback<String>() {
            @Override
            public void onSucces(String idCorso) {
                createStudent(idCorso,isTutor);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Errore generico");
            }
        });
    }

    private void createStudent(String idCorso, boolean isTutor) {
        CreateStudentRequest srequest = new CreateStudentRequest(idCorso, isTutor);

        studentRepository.createStudent(srequest, new Callback<StudentModel>() {
            @Override
            public void onSucces(StudentModel studentModel) {
                createStudentSuccess.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Errore generico");
            }
        });
    }


    public void checkTutorExistence() {
        String uid = userRepository.getCurrentUser().getUid();

        tutorRepository.tutorExist(uid, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                if (!exist) {
                    errorMessage.setValue("Tutor not exist");
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue(String.valueOf(R.string.generic_error));
            }
        });
    }
}
