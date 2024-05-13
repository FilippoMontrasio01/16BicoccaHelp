package com.example.bicoccahelp.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class CompleteStudentProfileFragment extends Fragment {

    private CompleteStudentProfileViewModel viewModel;
    private FragmentCompleteStudentProfileBinding binding;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CompleteStudentProfileViewModel.class);
        viewModel.initRepositories();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompleteStudentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        binding.createStudentButton.setOnClickListener(v -> createStudent());
        binding.studentBackButton.setOnClickListener(v -> navController.navigate(R.id.action_from_complete_profile_to_profile_fragment));

        observeViewModel();
    }

    private void createStudent() {
        String studyProgram = Objects.requireNonNull(binding.createStudentEditText.getText()).toString();
        boolean isMagistrale = binding.createStudentCheckBox.isChecked();
        boolean isTutor = binding.yesRadioButton.isChecked();

        viewModel.getStudyProgramLevel(studyProgram, isMagistrale, isTutor);
    }

    private void observeViewModel() {
        viewModel.getCreateStudentSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                if (binding.createStudentCheckBox.isChecked()) {
                    navController.navigate(R.id.action_from_complete_student_to_complete_tutor);
                } else {
                    navController.navigate(R.id.action_from_complete_profile_to_profile_fragment);
                }
            } else {
                Snackbar.make(requireView(), getString(R.string.create_student_error), Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
