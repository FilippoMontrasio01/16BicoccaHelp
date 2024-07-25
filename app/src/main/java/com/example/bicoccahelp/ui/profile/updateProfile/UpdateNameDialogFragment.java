package com.example.bicoccahelp.ui.profile.updateProfile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bicoccahelp.R;
import com.example.bicoccahelp.data.Callback;
import com.example.bicoccahelp.data.user.UserRepository;
import com.example.bicoccahelp.data.user.student.StudentModel;
import com.example.bicoccahelp.data.user.student.StudentRepository;
import com.example.bicoccahelp.data.user.tutor.TutorRepository;
import com.example.bicoccahelp.databinding.FragmentUpdateNameDialogBinding;
import com.example.bicoccahelp.ui.profile.ProfileViewModel;
import com.example.bicoccahelp.ui.profile.ProfileViewModelFactory;
import com.example.bicoccahelp.utils.InputValidator;
import com.example.bicoccahelp.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class UpdateNameDialogFragment extends DialogFragment implements View.OnClickListener{

    private UserRepository userRepository;
    private FragmentUpdateNameDialogBinding binding;
    private NavController navController;
    private StudentRepository studentRepository;
    private TutorRepository tutorRepository;

    private ProfileViewModel profileViewModel;



    public UpdateNameDialogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = ServiceLocator.getInstance().getUserRepository();
        studentRepository = ServiceLocator.getInstance().getStudentRepository();
        tutorRepository = ServiceLocator.getInstance().getTutorRepository();

        ProfileViewModelFactory factory = new ProfileViewModelFactory(userRepository, studentRepository, tutorRepository);

        profileViewModel = new ViewModelProvider(this, factory).get(ProfileViewModel.class);

        profileViewModel.getNameUpdated().observe(this, nameUpdated -> {
            if(nameUpdated){
                navController.navigate(R.id.action_from_update_name_dialog_to_profile_fragment);
            }else{
                Snackbar.make(requireView(), getString(R.string.name_update_error),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
            }
        });



        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateNameDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert this.getParentFragment() != null;
        navController = NavHostFragment.findNavController(this.getParentFragment());

        binding.updateNameButtonConfirm.setOnClickListener(this);
        binding.updateNameButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.updateNameButtonConfirm.getId()){
            this.onClickConfirm();
            return;
        }

        if(v.getId() == binding.updateNameButtonCancel.getId()){
            this.onClickCancel();
        }


    }

    private void onClickCancel() {
        Objects.requireNonNull(getDialog()).cancel();
    }

    private void onClickConfirm() {
        String newName = Objects.requireNonNull(binding.updateNameEditText.getText()).toString();

        if(newName.isEmpty() || !InputValidator.isValidName(newName)){
            binding.updateNameTextInputLayout.setError(getString(R.string.invalid_name));
            return;
        }

        binding.updateNameTextInputLayout.setError(null);

        profileViewModel.updateName(newName);
    }


    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}