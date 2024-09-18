package com.example.bicoccahelp.ui.profile.completeProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.corsoDiStudi.CorsoDiStudiRepository;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentCompleteTutorProfileBinding;
import com.example.bicoccahelp.ui.profile.completeProfile.CompleteProfileVIewModelFactory;
import com.example.bicoccahelp.ui.profile.completeProfile.CompleteProfileViewModel;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.Objects;


public class CompleteTutorProfileFragment extends Fragment implements View.OnClickListener{

    private FragmentCompleteTutorProfileBinding binding;
    private TutorRepository tutorRepository;
    private NavController navController;
    private CorsoDiStudiRepository corsoDiStudiRepository;
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private String livello = "Triennale";
    private CompleteProfileViewModel completeProfileViewModel;


    public CompleteTutorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        userRepository = ServiceLocator.getInstance().getUserRepository();
        corsoDiStudiRepository = ServiceLocator.getInstance().getCorsoDiStudiRepository();

        CompleteProfileVIewModelFactory factory = new CompleteProfileVIewModelFactory(corsoDiStudiRepository,
                userRepository, studentRepository, tutorRepository);

        completeProfileViewModel = new ViewModelProvider(this, factory).get(CompleteProfileViewModel.class);

        completeProfileViewModel.extractStudyProgram();
        completeProfileViewModel.extractLevel();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompleteTutorProfileBinding.inflate(inflater,
                container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.createTutorButton.setOnClickListener(this);
        binding.tutorBackButton.setOnClickListener(this);
        binding.addSubjectButton.setOnClickListener(this);


        observeViewModel();



    }

    @Override
    public void onClick(View v) {

        if(v.getId() == binding.createTutorButton.getId()){

            if (!completeProfileViewModel.isAnyDaySelected()) {
                // Mostra una Snackbar se nessun giorno è stato selezionato
                Snackbar.make(requireView(), "Please select at least one day of availability.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            String studyProgram = Objects.requireNonNull(binding.createTutorEditText
                    .getText()).toString();

            if(binding.livelloCheckbox.isChecked()) {
                livello = getString(R.string.magistrale);
            }

            completeProfileViewModel.validateStudyProgram(studyProgram, livello);
            return;
        }

        if(v.getId() == binding.tutorBackButton.getId()){
            backOnclick();
            return;
        }

        if(v.getId() == binding.addSubjectButton.getId()){
            addSubjectOnClick();
        }

    }

    private void addSubjectOnClick() {
        String skill = binding.bestSubjectEditText.getText().toString();

        if(skill.isEmpty()){
            binding.bestSubjectTextInputLayout.setError(getString(R.string.insert_skill));
            return;
        }

        completeProfileViewModel.addSkill(skill);
        binding.bestSubjectEditText.getText().clear();


    }

    private void backOnclick() {
        navController.navigate(R.id.action_from_complete_tutor_to_profile_fragment);
    }

    private void observeViewModel(){

        completeProfileViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        });

        completeProfileViewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), snackbarMessage -> {
            if (snackbarMessage != null) {
                Snackbar.make(requireView(), snackbarMessage, Snackbar.LENGTH_SHORT).show();
            }
        });

        completeProfileViewModel.getIsTutorCreated().observe(getViewLifecycleOwner(), isCreated -> {
            if (isCreated) {
                // Naviga a qualche altra parte se necessario
                navController.navigate(R.id.action_from_complete_tutor_to_profile_fragment);
            }
        });

        completeProfileViewModel.getCorsoName().observe(getViewLifecycleOwner(), corsoName -> {
            binding.createTutorEditText.setText(corsoName);
        });

        completeProfileViewModel.getCorsoLivello().observe(getViewLifecycleOwner(), level -> {

            binding.livelloCheckbox.setChecked(level.equals(getString(R.string.magistrale)));



        });

        completeProfileViewModel.getDisponibilitaGiorni().observe(getViewLifecycleOwner(), disponibilitaGiorni -> {
            if (disponibilitaGiorni != null) {
                // Aggiorna i CheckBox con le nuove disponibilità
                updateCheckBoxes(disponibilitaGiorni);
            }
        });

        completeProfileViewModel.getCorsoId().observe(getViewLifecycleOwner(), idCorso ->{

            if (idCorso != null) {
                completeProfileViewModel.createTutor(idCorso);
            }
        });
    }

    private void updateCheckBoxes(Map<String, Boolean> disponibilitaGiorni) {
        CheckBox[] checkBoxes = new CheckBox[]{
                binding.mondayCheckbox,
                binding.tuesdayCheckbox,
                binding.wednesdayCheckbox,
                binding.thursdayCheckbox,
                binding.fridayCheckbox,
                binding.saturdayCheckbox,
                binding.sundayCheckbox
        };

        for (CheckBox checkBox : checkBoxes) {
            Boolean isChecked = disponibilitaGiorni.get(checkBox.getText().toString());
            if (isChecked != null) {
                checkBox.setChecked(isChecked);
            } else {
                // Se non ci sono informazioni nel Map, setta il CheckBox a false
                checkBox.setChecked(false);
                // Aggiorna il Map con false per il giorno corrente
                disponibilitaGiorni.put(checkBox.getText().toString(), false);
            }

            checkBox.setOnCheckedChangeListener((buttonView, check) -> {
                completeProfileViewModel.updateDisponibilitaGiorni(checkBox.getText().toString(), check);
            });
        }
    }



    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}