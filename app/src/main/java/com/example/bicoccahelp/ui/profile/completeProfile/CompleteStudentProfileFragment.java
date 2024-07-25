package com.example.bicoccahelp.ui.profile.completeProfile;
import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.CreateStudentRequest;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentCompleteStudentProfileBinding;
import com.example.bicoccahelp.ui.profile.completeProfile.CompleteProfileVIewModelFactory;
import com.example.bicoccahelp.ui.profile.completeProfile.CompleteProfileViewModel;
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
    private CompleteProfileViewModel completeProfileViewModel;


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

        CompleteProfileVIewModelFactory factory = new CompleteProfileVIewModelFactory(corsoDiStudiRepository,
                userRepository, studentRepository, tutorRepository);

        completeProfileViewModel = new ViewModelProvider(this, factory).get(CompleteProfileViewModel.class);


        completeProfileViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

        completeProfileViewModel.extractStudyProgram();
        completeProfileViewModel.extractLevel();


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
        navController = Navigation.findNavController(view);
        binding.createStudentButton.setOnClickListener(this);
        binding.studentBackButton.setOnClickListener(this);


        completeProfileViewModel.getCorsoId().observe(getViewLifecycleOwner(), idCorso -> {
            Log.d(TAG, "CorsoId aggiornato: " + idCorso);
            if (idCorso != null) {
                if (binding.yesRadioButton.isChecked()) {
                    Log.d(TAG, "yesRadioButton è selezionato");
                    yesRadioButtonAnswer(idCorso);
                } else if (binding.noRadioButton.isChecked()) {
                    Log.d(TAG, "noRadioButton è selezionato");
                    noRadioButtonAnswer(idCorso);
                } else {
                    Log.d(TAG, "Nessun radio button selezionato");
                    Snackbar.make(requireView(), getString(R.string.tutor_radioButton), Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "CorsoId è null");
                Snackbar.make(requireView(), "QUALCOSA NON FUNZIONA", Snackbar.LENGTH_SHORT).show();
            }
        });

        completeProfileViewModel.getIsStudentCreated().observe(getViewLifecycleOwner(), isStudentCreated -> {
            if (isStudentCreated) {
                navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
            } else {
                Snackbar.make(requireView(), getString(R.string.generic_error), Snackbar.LENGTH_SHORT).show();
            }
        });


        completeProfileViewModel.getCorsoName().observe(getViewLifecycleOwner(), corsoName -> {
            binding.createStudentEditText.setText(corsoName);
        });

        completeProfileViewModel.getCorsoLivello().observe(getViewLifecycleOwner(), level -> {
            binding.createStudentCheckBox.setChecked(level.equals(getString(R.string.magistrale)));
        });

        completeProfileViewModel.isTutor(new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean isTutor) {
                if(isTutor){
                    binding.yesRadioButton.setChecked(true);
                    binding.yesRadioButton.setEnabled(false);
                    binding.noRadioButton.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.createStudentButton.getId()){
            createStudentOnClick();
            return;
        }

        if(v.getId() == binding.studentBackButton.getId()){
            backOnClick();
        }
    }

    private void backOnClick() {
        navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
    }

    private void createStudentOnClick() {
        String studyProgram = Objects.requireNonNull(binding.createStudentEditText.getText()).toString();

        if (binding.createStudentCheckBox.isChecked()) {
            livello = getString(R.string.magistrale);
        }

        completeProfileViewModel.validateStudyProgram(studyProgram, livello);
    }


    public void noRadioButtonAnswer(String idCorso){
        CreateStudentRequest srequest = new CreateStudentRequest(idCorso, false);
        completeProfileViewModel.createStudent(srequest);
    }

    public void yesRadioButtonAnswer(String idCorso){
        CreateStudentRequest srequest = new CreateStudentRequest(idCorso, true);
        completeProfileViewModel.createStudentTutor(srequest);
        completeProfileViewModel.isTutor(new Callback<Boolean>() {
            @Override
            public void onSucces(Boolean tutorExist) {
                if (tutorExist) {
                    navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
                } else {
                    navController.navigate(R.id.action_from_complete_student_to_complete_tutor);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(requireView(), "Errore nel controllo del tutor", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}


