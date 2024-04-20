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
import com.example.bicoccahelp.data.user.UserModel;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.databinding.FragmentCompleteStudentProfileBinding;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CompleteStudentProfileFragment extends Fragment implements View.OnClickListener{



    private StudentRepository studentRepository;
    private UserRepository userRepository;
    private FragmentCompleteStudentProfileBinding binding;
    private NavController navController;
    private CorsoDiStudiRepository corsoDiStudiRepository;


    public CompleteStudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompleteStudentProfileBinding.inflate(inflater, container, false);
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
        String studyProgram = Objects.requireNonNull(binding.createStudentEditText.getText()).toString();


        if(!InputValidator.isValidStudyProgram(studyProgram)){
            return;
        }


        corsoDiStudiRepository.getCorsoDiStudiIdByName(studyProgram, new Callback<String>() {
            @Override
            public void onSucces(String idCorso) {
                if(binding.noRadioButton.isChecked()){
                    UserModel user = userRepository.getCurrentUser();
                    CreateStudentRequest srequest = new CreateStudentRequest(idCorso, false);

                    studentRepository.createStudent(srequest, new Callback<StudentModel>() {
                        @Override
                        public void onSucces(StudentModel studentModel) {
                            navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
                            requireActivity().finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Snackbar.make(requireView(), "ERRORE", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }

                if(binding.yesRadioButton.isChecked()){
                    UserModel user = userRepository.getCurrentUser();
                    CreateStudentRequest srequest = new CreateStudentRequest(idCorso, true);

                    studentRepository.createStudent(srequest, new Callback<StudentModel>() {
                        @Override
                        public void onSucces(StudentModel studentModel) {
                            navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
                            requireActivity().finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Snackbar.make(requireView(), "ERRORE", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), "NON ESISTE IL CORSO", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

}