package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentCompleteStudentProfileBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CompleteStudentProfileFragment extends Fragment implements View.OnClickListener{



    private StudentRepository studentRepository;
    private FragmentCompleteStudentProfileBinding binding;
    private NavController navController;
    private UserRepository userRepository;
    private TutorRepository tutorRepository;
    private String livello = "Triennale";
    private CorsoDiStudiRepository corsoDiStudiRepository;


    public CompleteStudentProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompleteStudentProfileBinding.inflate(inflater,
                container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        binding.createStudentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.createStudentButton.getId()){
            createStudentOnClick();
        }
    }

    private void createStudentOnClick() {
        String studyProgram = Objects.requireNonNull(binding.createStudentEditText
                .getText()).toString();



        if(binding.createStudentCheckBox.isChecked()) {
            livello = getString(R.string.magistrale);
        }
        InputValidator.isValidStudyProgram(studyProgram, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                if(exist){
                    getCorsoId(studyProgram, livello);
                }else{
                    binding.createStudentTextInputLayout
                            .setError(getString(R.string.insert_a_valid_studyProgram));
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }



    public void getCorsoId(String studyProgram, String livello){


        corsoDiStudiRepository.getCorsoDiStudiIdByName(
                studyProgram, livello, new Callback<String>() {
            @Override
            public void onSucces(String idCorso) {

                if(binding.yesRadioButton.isChecked()) {
                    yesRadioButtonAnswer(idCorso);
                }else if(binding.noRadioButton.isChecked()){
                    noRadioButtonAnswer(idCorso);
                }else{
                    Snackbar.make(requireView(), getString(R.string.tutor_radioButton),
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void noRadioButtonAnswer(String idCorso){
        CreateStudentRequest srequest = new CreateStudentRequest(idCorso, false);

        studentRepository.createStudent(srequest, new Callback<StudentModel>() {
            @Override
            public void onSucces(StudentModel studentModel) {
                navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void yesRadioButtonAnswer(String idCorso){
        CreateStudentRequest srequest = new CreateStudentRequest(idCorso, true);

        studentRepository.createStudent(srequest, new Callback<StudentModel>() {
            @Override
            public void onSucces(StudentModel studentModel) {
                tutorExist();
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void tutorExist(){
        String uid = userRepository.getCurrentUser().uid;

        tutorRepository.tutorExist(uid, new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean exist) {
                if(!exist){
                    navController.navigate(R.id.action_from_complete_student_to_complete_tutor);
                }else{
                    navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), getString(R.string.generic_error),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }


}