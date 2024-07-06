package com.example.bicoccahelp.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.CreateTutorRequest;
import com.example.bicoccahelp.data.user.tutor.TutorModel;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.utils.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompleteProfileViewModel extends ViewModel {
    private final CorsoDiStudiRepository corsoDiStudiRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    private MutableLiveData<Boolean> isTutor;
    private MutableLiveData<Boolean> isStudentCreated;

    private MutableLiveData<Boolean> isStudent;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<String> corsoId;
    private MutableLiveData<String> corsoName;

    private MutableLiveData<Boolean> isMagistrale;

    private ArrayList<String> subject = new ArrayList<>();
    private  MutableLiveData<String> snackbarMessage;
    private  MutableLiveData<Map<String, Boolean>> disponibilitaGiorni;
    private MutableLiveData<Boolean> isTutorCreated;
    private MutableLiveData<String> corsoLivello;


    public CompleteProfileViewModel(CorsoDiStudiRepository corsoDiStudiRepository,
                                    UserRepository userRepository,
                                    StudentRepository studentRepository,
                                    TutorRepository tutorRepository) {
        this.corsoDiStudiRepository = corsoDiStudiRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;

        this.isTutor = new MutableLiveData<>();
        this.isStudentCreated = new MutableLiveData<>();
        this.isTutorCreated = new MutableLiveData<>();
        this.isStudent = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
        this.snackbarMessage = new MutableLiveData<>();
        this.disponibilitaGiorni = new MutableLiveData<>();
        this.corsoId = new MutableLiveData<>();
        this.isMagistrale = new MutableLiveData<>();
        this.corsoName = new MutableLiveData<>();
        this.corsoLivello = new MutableLiveData<>();

        disponibilitaGiorni.setValue(new HashMap<>());

    }

    public LiveData<Boolean> getIsTutor() {
        return isTutor;
    }

    public LiveData<Boolean> getIsTutorCreated() {
        return isTutorCreated;
    }

    public LiveData<Boolean> getIsStudentCreated() {
        return isStudentCreated;
    }

    public LiveData<String> getCorsoName() {
        return corsoName;
    }

    public MutableLiveData<String> getCorsoLivello() {
        return corsoLivello;
    }

    public LiveData<String> getErrorMessage(){
        return errorMessage;
    }

    public LiveData<String> getCorsoId() {
        return corsoId;
    }

    public LiveData<String> getSnackbarMessage() {
        return snackbarMessage;
    }

    public LiveData<Map<String, Boolean>> getDisponibilitaGiorni() {
        return disponibilitaGiorni;
    }

    public void updateDisponibilitaGiorni(String giorno, boolean isChecked) {
        Map<String, Boolean> disponibilita = disponibilitaGiorni.getValue();
        if (disponibilita == null) {
            disponibilita = new HashMap<>();
        }
        disponibilita.put(giorno, isChecked);
        disponibilitaGiorni.setValue(disponibilita);
    }

    public void addSkill(String skill) {
        if (skill.isEmpty()) {
            snackbarMessage.setValue("Insert a skill");
            return;
        }
        subject.add(skill);
        snackbarMessage.setValue("Skill added: " + skill);
    }

    public void validateStudyProgram(String program, String livello) {
        InputValidator.isValidStudyProgram(program, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                if (exist) {
                    getCorsoId(program, livello);
                } else {
                    errorMessage.setValue("Invalid study program");
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error validating study program");
            }
        });
    }

    public void getCorsoId(String studyProgram, String livello){

        corsoDiStudiRepository.getCorsoDiStudiIdByName(studyProgram, livello, new Callback<String>() {
            @Override
            public void onSucces(String idCorso) {
                corsoId.setValue(idCorso);
                studentExist();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    public void extractStudyProgram(){
        String uidUser = userRepository.getCurrentUser().getUid();

        studentRepository.getCorsoDiStudi(uidUser, new Callback<String>() {
            @Override
            public void onSucces(String idStudyProgram) {
                corsoDiStudiRepository.getCorsodiStudiName(idStudyProgram, new Callback<String>() {
                    @Override
                    public void onSucces(String s) {
                        corsoName.setValue(s);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        errorMessage.setValue("zio cane");
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void extractLevel(){
        String uidUser = userRepository.getCurrentUser().getUid();

        studentRepository.getCorsoDiStudi(uidUser, new Callback<String>() {
            @Override
            public void onSucces(String idCorso) {
                corsoDiStudiRepository.getCorsoDiStudiLivello(idCorso, new Callback<String>() {
                    @Override
                    public void onSucces(String livello) {
                        corsoLivello.setValue(livello);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        errorMessage.setValue("fuckkk");
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }


    public void studentExist(){
        String uidUser = userRepository.getCurrentUser().getUid();

        studentRepository.studentExist(uidUser, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean studentExist) {
                if(studentExist){
                    isStudent.setValue(true);
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Lo studente non esiste");
            }
        });
    }

    public void createStudent(CreateStudentRequest request){
        studentRepository.createStudent(request, new Callback<StudentModel>() {
            @Override
            public void onSucces(StudentModel studentModel) {
                isStudentCreated.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Student not created");
            }
        });
    }


    public void createStudentTutor(CreateStudentRequest request){
        studentRepository.createStudent(request, new Callback<StudentModel>() {
            @Override
            public void onSucces(StudentModel studentModel) {
                isTutor.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Student and tutor not created");
            }
        });
    }

    public void createTutor(String idCorso) {
        String uid = userRepository.getCurrentUser().getUid();

        tutorRepository.tutorExist(uid, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {

                if (!exist) {
                    Map<String, Boolean> map = disponibilitaGiorni.getValue();
                    if (map == null || map.isEmpty()) {
                        errorMessage.setValue("Availability error");
                        return;
                    }
                }

                if (idCorso != null) {
                    CreateTutorRequest request = new CreateTutorRequest(idCorso, disponibilitaGiorni.getValue(), subject);

                    tutorRepository.createTutor(request, new Callback<TutorModel>() {
                        @Override
                        public void onSucces(TutorModel tutorModel) {

                            studentRepository.studentExist(uid, new Callback<Boolean>() {
                                @Override
                                public void onSucces(Boolean aBoolean) {
                                    if (exist) {
                                        studentRepository.updateiSTutor(uid, true);
                                    }
                                    isTutorCreated.setValue(true);
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    errorMessage.setValue("Failed to update student");
                                }
                            });

                        }

                        @Override
                        public void onFailure(Exception e) {
                            errorMessage.setValue("Failed to create tutor");
                        }
                    });

                } else {
                    errorMessage.setValue("Course ID is null");
                }

            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Failed to check tutor status");
            }
        });
    }

    public void isTutor(Callback<Boolean> callback) {
        String uidUser = userRepository.getCurrentUser().getUid();
        tutorRepository.tutorExist(uidUser, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean tutorExist) {
                callback.onSucces(tutorExist); // Passa il risultato al callback
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Tutor not found");
                callback.onFailure(e); // Passa l'errore al callback
            }
        });
    }


    protected void onCleared() {
        super.onCleared();
        // Clean up resources if needed
    }

    /*public void tutorExist(){
        String uid = userRepository.getCurrentUser().getUid();

        tutorRepository.tutorExist(uid, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean tutorExist) {
                if(tutorExist){
                    isTutor.setValue(true);
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error: Tutor not found");
            }
        });
    }*/


}
